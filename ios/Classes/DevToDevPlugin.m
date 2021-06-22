#import "DevToDevPlugin.h"
#if __has_include(<dev_to_dev/dev_to_dev-Swift.h>)
#import <dev_to_dev/dev_to_dev-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "dev_to_dev-Swift.h"
#endif

@implementation DevToDevPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftDevToDevPlugin registerWithRegistrar:registrar];
}
@end
