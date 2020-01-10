import { NativeModules } from 'react-native';

const { Geofence } = NativeModules;


const Events = {
	EXIT: 'onExit',
	ENTER: 'onEnter'
}

export {
	Events
}


export default {
	add: geofenceProperites => {
		return new Promise((resolve, reject) => {
			Geofence.add(geofenceProperites)
				.then(id => resolve(id))
				.catch(e => reject(e))
		})
	}
};