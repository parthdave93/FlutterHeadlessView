#import "FlutterBackgroundPlugin.h"
#import <flutter_background_plugin/flutter_background_plugin-Swift.h>

@implementation FlutterBackgroundPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftFlutterBackgroundPlugin registerWithRegistrar:registrar];
}
@end
