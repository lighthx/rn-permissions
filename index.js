
import { NativeModules } from 'react-native';
import {Platform,BackHandler,Alert} from 'react-native';
import Toast from 'react-native-simple-toast'
const { RNPermissions } = NativeModules;
 async function checkStatusTodo(){
    const result =await RNPermissions.getStatus();
    console.log(result)
    const isIOS=Platform.OS==="ios"
    const {status}=result
    if(!__DEV__&&status==="SHUT"){
        Alert.alert("注意","您的手机存在高风险软件,无法使用APP!",[{text:"确定",onPress:()=>isIOS?RNPermissions.iosExit():BackHandler.exitApp()}])
    }else if(!__DEV__&&status==="WARN"){
        Toast.show(isIOS?"检测到您的设备正处于越狱或者其他不安全环境中运行，请谨慎使用!":"您的手机已被root,存在一定风险")
    }
}
export default {checkStatusTodo}
