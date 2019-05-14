
#import "RNPermissions.h"

@implementation RNPermissions

RCT_EXPORT_MODULE();

RCT_REMAP_METHOD(getStatus , findEventsWithResolver:(RCTPromiseResolveBlock)resolve         rejecter:(RCTPromiseRejectBlock)reject)
{
    NSArray *events = [PermissionsDetection detectionDevice];
    if (events) {
        resolve(events);
    } else {
        NSError *error = [NSError init];
        reject(@"no_events", @"There were no events",error);
    }
}
RCT_EXPORT_METHOD(iosExit ){
    exit(0);
}

@end
  
