module.exports = {
    init: function (successCalback, errorCallback){
        console.log("we are in init method !!");
       	cordova.exec(successCalback, errorCallback, "IHealthBinder", "init", []);
    },
    setUserId: function (model, address, userId, successCalback, errorCallback){
        console.log("we are in setUserId method !!");
        cordova.exec(successCalback, errorCallback, "IHealthBinder", "setUserId",
        [{
            "model": model,
            "address": address,
            "user_id": userId
        }]);
    },
    getUserId: function (model, address, successCalback, errorCallback){
        console.log("we are in getUserId method !!");
       	cordova.exec(successCalback, errorCallback, "IHealthBinder", "getUserId",
        [{
            "model": model,
            "address": address
        }]);
    },
    setUserInfo: function (model, address, age, height, weight, gender, unit, target, activityLevel, min, successCalback, errorCallback){
        console.log("we are in setUserInfo method !!");
    	cordova.exec(successCalback, errorCallback, "IHealthBinder", "setUserInfo",
    	[{
    	   "model": model,
    	   "address": address,
    	   "age": age,
    	   "height": height,
    	   "weight": weight,
    	   "gender": gender,
    	   "unit": unit,
    	   "target": target,
    	   "activityLevel": activityLevel,
    	   "min": min,
    	}]);
    },
	startDiscovery: function (model, successCalback, errorCallback){
    	console.log("we are in startscan method !!");
		cordova.exec(successCalback, errorCallback, "IHealthBinder", "startDiscovery",
		 [{
		    "model": model
		 }]);
	},
	stopDiscovery: function (successCalback, errorCallback){
       	console.log("we are in stopscan method !!");
   		cordova.exec(successCalback, errorCallback, "IHealthBinder", "stopDiscovery", []);
   	},
   	registerOnScannedDevice: function (successCalback, errorCallback){
   	    console.log("we are in registerOnScannedDevice method !!");
       	cordova.exec(successCalback, errorCallback, "IHealthBinder", "registerOnScannedDevice", []);
    },
   	registerOnDiscoveryFinished: function (successCalback, errorCallback){
   	    console.log("we are in registerOnDiscoveryFinished method !!");
       	cordova.exec(successCalback, errorCallback, "IHealthBinder", "registerOnDiscoveryFinished", []);
   	},
   	registerOnDeviceDisconnected: function (successCalback, errorCallback){
       	console.log("we are in registerOnDeviceDisconnected method !!");
        cordova.exec(successCalback, errorCallback, "IHealthBinder", "registerOnDeviceDisconnected", []);
    },
    registerOnDeviceConnectionFailed: function (successCalback, errorCallback){
           	console.log("we are in registerOnDeviceConnectionFailed method !!");
            cordova.exec(successCalback, errorCallback, "IHealthBinder", "registerOnDeviceConnectionFailed", []);
    },
  	connect: function(address, successCalback, errorCallback){
       	console.log("we are in connect method !!");
       	console.log(address);
    	cordova.exec(successCalback, errorCallback, "IHealthBinder", "connect",
    	 [{
    	    "address": address
    	 }]);
    },
    disconnect: function(model, address, successCalback, errorCallback){
       	console.log("we are in diconnect method !!");
       	cordova.exec(successCalback, errorCallback, "IHealthBinder", "disconnect",
       	 [{
       	    "model": model,
       	    "address": address
       	 }]);
    },
    sendRandomNumber: function(model, address, successCalback, errorCallback){
           	console.log("we are in sendRandomNumber method !!");
           	cordova.exec(successCalback, errorCallback, "IHealthBinder", "sendRandomNumber",
           	 [{
           	    "model": model,
           	    "address": address
           	 }]);
        },
   	getActivityData: function (model, address, successCalback, errorCallback){
       	console.log("we are in getActivityData method !!");
   		cordova.exec(successCalback, errorCallback, "IHealthBinder", "getActivityData",
   		 [{
       	    "model": model,
       	    "address": address
   		 }]);
   	},
    getSleepData: function (model, address, successCalback, errorCallback){
        console.log("we are in getSleepData method !!");
        cordova.exec(successCalback, errorCallback, "IHealthBinder", "getSleepData",
         [{
            "model": model,
            "address": address
         }]);
    },
    syncTime: function (model, address, successCalback, errorCallback){
        console.log("we are in syncTime method !!");
        cordova.exec(successCalback, errorCallback, "IHealthBinder", "syncTime",
         [{
            "model": model,
            "address": address
         }]);
    },
    setTimeMode: function (model, address, mode, successCalback, errorCallback){
        console.log("we are in syncTime method !!");
        cordova.exec(successCalback, errorCallback, "IHealthBinder", "setTimeMode",
        [{
            "model": model,
            "address": address,
            "mode": mode
        }]);
    }
};