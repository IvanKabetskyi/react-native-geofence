import { NativeModules, NativeAppEventEmitter, AppRegistry } from 'react-native';

const { Geofence } = NativeModules;

const geofenceEventEmitter = new NativeAppEventEmitter(Geofence);

const Events = {
	EXIT: 'onExit',
	ENTER: 'onEnter'
}

export {
	Events
}

const HeadlessGeofenceEventTask = async (({event, ids}) => {
	console.log(event ,ids);
	geofenceEventEmitter.emit(event, ids);
});

AppRegistry.registerHeadlessTask('OnGeofenceEvent', () => HeadlessGeofenceEventTask);


export default {
	add: geofenceProperites => {
		return new Promise((resolve, reject) => {
			Geofence.add(geofenceProperites)
				.then(id => resolve(id))
				.catch(e => reject(e))
		})
	}
};
