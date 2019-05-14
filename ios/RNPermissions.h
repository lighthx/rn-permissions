
#if __has_include("RCTBridgeModule.h")
#import "RCTBridgeModule.h"
#else
#import <React/RCTBridgeModule.h>
#endif
#import "PermissionsDetection.h"
@interface RNPermissions : NSObject <RCTBridgeModule>

@end
  
