
# rn-permissions

## Getting started

`$ npm install rn-permissions --save`

### Mostly automatic installation

`$ react-native link rn-permissions`

### Manual installation


#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `rn-permissions` and add `RNPermissions.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNPermissions.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.reactlibrary.RNPermissionsPackage;` to the imports at the top of the file
  - Add `new RNPermissionsPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':rn-permissions'
  	project(':rn-permissions').projectDir = new File(rootProject.projectDir, 	'../node_modules/rn-permissions/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':rn-permissions')
  	```

#### Windows
[Read it! :D](https://github.com/ReactWindows/react-native)

1. In Visual Studio add the `RNPermissions.sln` in `node_modules/rn-permissions/windows/RNPermissions.sln` folder to their solution, reference from their app.
2. Open up your `MainPage.cs` app
  - Add `using Permissions.RNPermissions;` to the usings at the top of the file
  - Add `new RNPermissionsPackage()` to the `List<IReactPackage>` returned by the `Packages` method


## Usage
```javascript
import RNPermissions from 'rn-permissions';

// TODO: What to do with the module?
RNPermissions;
```
  