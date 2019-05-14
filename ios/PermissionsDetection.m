//
//  PermissionsDetection.m
//  Finance
//
//  Created by Nick on 2018/9/17.
//  越狱权限检测

#import "PermissionsDetection.h"
#import <sys/stat.h>
#import <dlfcn.h>
#import <mach-o/dyld.h>

@implementation PermissionsDetection

/**
 检测设备是否越狱
 */
+ (id)detectionDevice
{
    PermissionsDetection *obj = [[PermissionsDetection alloc] init];
    
    BOOL is_0 = [obj isJailbroken];         // 越狱
    BOOL is_1 = [obj checkAppList];         // 越狱
    BOOL is_2 = [obj checkCydia];           // 越狱
    BOOL is_3 = [obj checkInject];          // hook
    BOOL is_4 = [obj checkDylibs];          // hook
    BOOL is_5 = [obj printEnv];             // 越狱
    
    if(is_3 || is_4) // 强制退出
    {
        return @{@"status":@"SHUT"};
//        [UI_AlertView showAlertControllerWithTitle:@"您的手机存在高风险软件" message:@"无法使用APP\n建议使用小雨点网页版(m.xyd.cn)" okButtonTitle:@"确定" okClickHandle:^{
//
//            exit(0);
//
//        }];
    }
    else if(is_0 || is_1 || is_2 || is_5)
    {
        return @{@"status":@"WARN"};
//        [UI_AlertView showAlertControllerWithTitle:@"" message:@"检测到您的设备正处于越狱或者其他不安全环境中运行，请谨慎使用!" okButtonTitle:@"确定" okClickHandle:nil];
    }else{
        return @{@"status":@"SUCCESS"};
    }
    
}

/*
 1、写成BOOL开关方法，给攻击者直接锁定目标hook绕过的机会
 2、攻击者可能会改变这些工具的安装路径，躲过判断
 /Applications/Cydia.app
 /Library/MobileSubstrate/MobileSubstrate.dylib
 /bin/bash
 /usr/sbin/sshd
 /etc/apt
 */
- (BOOL) isJailbroken{
    if ([[NSFileManager defaultManager] fileExistsAtPath:@"/Applications/Cydia.app"]){
        return YES;
    }
    // ...
    return NO;
}

/*
 尝试读取下应用列表，看看有无权限获取
 缺陷：攻击者可能会hook NSFileManager 的方法
 */
- (BOOL) checkAppList{
    
    BOOL is = NO;
    
    if ([[NSFileManager defaultManager] fileExistsAtPath:@"/User/Applications/"]){
        
        @try
        {
            NSArray *applist = [[NSFileManager defaultManager] contentsOfDirectoryAtPath:@"/User/Applications/"                                                                 error:nil];
            
            if(applist || applist.count > 0)
                is = YES;
        }
        @catch(NSException *e)
        {
            NSLog(@"Exception : %@", e);
            
            is = YES;
        }
        
    }else{
        NSLog(@"Device is not jailbroken");
        is = NO;
    }
    return is;
}

/*
 使用stat系列函数检测Cydia等工具
 缺陷：攻击者可能会利用 Fishhook原理 hook了stat。
 */
- (BOOL) checkCydia
{
    BOOL is = NO;
    
    struct stat stat_info;
    if (0 == stat("/Applications/Cydia.app", &stat_info)) { // 已越狱
        is = YES;
    }else{                                                  // 未越狱
        is = NO;
    }
    
    return is;
}

/*
 stat是否出自系统库，是否被攻击者换掉
 如果结果不是 /usr/lib/system/libsystem_kernel.dylib 的话，那就100%被攻击了。
 缺陷：攻击者可能替换libsystem_kernel.dylib 绕过检测。
 */
- (BOOL) checkInject
{
    BOOL is = NO;
    
    int ret ;
    Dl_info dylib_info;
    int    (*func_stat)(const char *, struct stat *) = stat;
    if ((ret = dladdr(func_stat, &dylib_info))) {
        NSLog(@"lib :%s", dylib_info.dli_fname);
        
        NSString *str = [[NSString alloc] initWithCString:dylib_info.dli_fname encoding:NSASCIIStringEncoding];
        
        if(![str containsString:@"usr/lib/system/libsystem_kernel.dylib"])
        { // 被攻击
            is = YES;
        }
        else
        { // 被攻击
            is = NO;
        }
    }
    return is;
}

/*
 检索一下应用程序是否被链接了异常动态库
 如果包含越狱机的输出结果会包含字符串： Library/MobileSubstrate/MobileSubstrate.dylib ，那可能被攻击了。
 缺陷：攻击者可能会给MobileSubstrate改名， 绕过检测。但是原理都是通过DYLD_INSERT_LIBRARIES注入动态库
 */
- (BOOL) checkDylibs
{
    BOOL is = NO;
    
    uint32_t count = _dyld_image_count();
    for (uint32_t i = 0 ; i < count; ++i) {
        NSString *name = [[NSString alloc]initWithUTF8String:_dyld_get_image_name(i)];
        NSLog(@"--%@", name);
        
        if([name containsString:@"Library/MobileSubstrate/MobileSubstrate.dylib"])
        {
            is = YES;
        }
    }
    return is;
}

/*
 检测当前程序运行的环境变量
 未越狱设备返回结果是null，越狱设备就各有各的精彩
 */
- (BOOL) printEnv
{
    char *env = getenv("DYLD_INSERT_LIBRARIES");
    if(!env)
    { // 未越狱
        return NO;
    }
    else
    { // 已越狱
        return YES;
    }
}

@end
