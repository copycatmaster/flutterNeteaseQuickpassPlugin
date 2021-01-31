#import <Flutter/Flutter.h>

@interface FlutterNeteasequickpassPlugin : NSObject<FlutterPlugin>
{
    
}
@property long long lastTokenTime;
@property (nonatomic,copy) NSString * lastToken;
@property (nonatomic,copy) NSString * jsonConfig;

@end
