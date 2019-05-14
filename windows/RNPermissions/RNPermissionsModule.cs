using ReactNative.Bridge;
using System;
using System.Collections.Generic;
using Windows.ApplicationModel.Core;
using Windows.UI.Core;

namespace Permissions.RNPermissions
{
    /// <summary>
    /// A module that allows JS to share data.
    /// </summary>
    class RNPermissionsModule : NativeModuleBase
    {
        /// <summary>
        /// Instantiates the <see cref="RNPermissionsModule"/>.
        /// </summary>
        internal RNPermissionsModule()
        {

        }

        /// <summary>
        /// The name of the native module.
        /// </summary>
        public override string Name
        {
            get
            {
                return "RNPermissions";
            }
        }
    }
}
