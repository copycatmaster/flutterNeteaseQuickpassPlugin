#import "FlutterNeteasequickpassPlugin.h"
#import <Flutter/Flutter.h>
#import <NTESQuickPass/NTESQuickPass.h>


@implementation FlutterNeteasequickpassPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  FlutterMethodChannel* channel = [FlutterMethodChannel
      methodChannelWithName:@"flutter_neteasequickpass"
            binaryMessenger:[registrar messenger]];
  FlutterNeteasequickpassPlugin* instance = [[FlutterNeteasequickpassPlugin alloc] init];
  [registrar addMethodCallDelegate:instance channel:channel];

  // if (![rootViewController isKindOfClass:[FlutterViewController class]]) {
  //   NSLog(@"expected FlutterViewController as rootViewController.");
  //   return NO;
  // }
  // FlutterViewController *flutterViewController = (FlutterViewController *)rootViewController;


}


- (void)handleMethodCall:(FlutterMethodCall*)call result:(FlutterResult)result {
    __weak typeof(self) weakSelf = self;
    if ([@"initialize" isEqualToString:call.method]) {
        NSString *businessID = call.arguments[@"businessId"];
        NSString *jsonConfig = call.arguments[@"jsonConfig"];
        long timeout = call.arguments[@"timeout"];
        NSLog(@"businessID is %@, jsonConfig is %@, timeout is %l",businessID,jsonConfig,timeout);
        NTESQuickLoginManager * loginMgr = [NTESQuickLoginManager sharedInstance];
        BOOL ok = [loginMgr shouldQuickLogin];
        [loginMgr registerWithBusinessID:businessID];
        result(@{@"code":@"ok",@"msg":@"ok",@"data":@{}});
        // NSData *jsonData = [jsonConfig dataUsingEncoding:NSUTF8StringEncoding];
        // NSError *err;
        // NSDictionary *dic = [NSJSONSerialization JSONObjectWithData:jsonData
        //                                                 options:NSJSONReadingMutableContainers
        //                                                   error:&err];
        // if(err) {
        //     NSLog(@"json解析失败：%@",err);
        //     return nil;
        // }

        //UIViewController *rootViewController = [[[[UIApplication sharedApplication] delegate] window] rootViewController];
        
    } else if ([@"getOperatorType" isEqualToString:call.method]) {
        result(@{@"code":@"ok",@"msg":@"ok",@"data":@{
                         @"isp":@"unknown"
        }});
    } else if ([@"prefetchToken" isEqualToString:call.method]) {
        [[NTESQuickLoginManager sharedInstance] getPhoneNumberCompletion:^(NSDictionary * _Nonnull resultDic) {
                NSNumber *boolNum = [resultDic objectForKey:@"success"];
                //WeakSelf(self);
                NSLog(@" resultDic: %@",resultDic);
                BOOL success = [boolNum boolValue];
                if (success) {
                    NSString* token = [resultDic objectForKey:@"token"];
                    weakSelf.lastToken  = token;
                    weakSelf.lastTokenTime = (long long)[[NSDate date] timeIntervalSince1970];
                    NTESQuickLoginModel *model = [[NTESQuickLoginModel alloc] init];
                    UIViewController *rootViewController = [[[[UIApplication sharedApplication] delegate] window] rootViewController];
                    model.currentVC = rootViewController;
                    model.presentDirectionType = NTESPresentDirectionPush;
                    model.authWindowPop = NTESAuthWindowPopCenter;
                    model.backgroundColor = [UIColor whiteColor];
                    model.privacyState = true;
                    model.logoImg = [UIImage imageNamed:@"app.png"];
                    [[NTESQuickLoginManager sharedInstance] setupModel:model];
                    // 设置授权登录界面model。注意：必须调用，此方法需嵌套在getPhoneNumberCompletion的回调中使用，且在CUCMAuthorizeLoginCompletion:之前调用。
                    // 电信获取脱敏手机号成功 需在此回调中拉去授权登录页面
                    result(@{@"code":@"ok",@"msg":@"ok",@"data":@{@"ydToken":token,
                        @"mobileNumber":[resultDic objectForKey:@"securityPhone"]
                    }});
                    // 移动、联通无脱敏手机号，需在此回调中拉去授权登录页面
                } else {
                     // 电信获取脱敏手机号失败
                     // 移动、联通预取号失败
                    result([FlutterError errorWithCode: @"error" message: [NSString stringWithFormat:@"获取失败:%@", [resultDic objectForKey:@"desc"]] details:@{
                        @"resultCode":[resultDic objectForKey:@"desc"]
                    }]);
                }
            }];
    } else if ([@"launchLogin" isEqualToString:call.method]) {
        if(weakSelf.lastToken == nil || ((long long)[[NSDate date] timeIntervalSince1970] - weakSelf.lastTokenTime) > 2*60*1000 ) {
            result([FlutterError errorWithCode: @"needPrefetchToken" message: @"需要调用prefetchToken" details:@{}]);
                  return;
        }
        
        [[NTESQuickLoginManager sharedInstance] CUCMCTAuthorizeLoginCompletion:^(NSDictionary * _Nonnull resultDic) {
               NSNumber *boolNum = [resultDic objectForKey:@"success"];
               BOOL success = [boolNum boolValue];
               if (success) {
                   // 取号成功，获取acessToken
                   NSLog(@" resultDic: %@",resultDic);
                   result(@{@"code":@"ok",@"msg":@"ok",@"data":@{
                        @"accessCode":[resultDic objectForKey:@"accessToken"],
                        @"ydToken":self.lastToken
                   }});
                   return;
               } else {
                    // 取号失败
                   result([FlutterError errorWithCode: @"error" message: @"获取失败" details:@{}]);
               }
             }];
    } else if ([@"quitLogin" isEqualToString:call.method]) {
        result(@{@"code":@"ok",@"msg":@"ok",@"data":@{}});
    } else {
        result([FlutterError errorWithCode: @"noSuchMethod" message: @"没有该方法" details:@{@"methodName":call.method}]);
    }
}

@end
