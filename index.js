import { NativeModules, DeviceEventEmitter  } from 'react-native';


const ValidicAggregatorFit = NativeModules.ValidicAggregatorFit || {}

ValidicAggregatorFit.eventEmitter = DeviceEventEmitter;

export default ValidicAggregatorFit;

/**
 * @namespace ValidicAggregatorFit
 * @description
 * 
 * The Validic Mobile library provides a simple way to read and upload data from S Health to Validic. Through {ValidicAggregatorFit}
 * you can subscribe to specific Google Fit data types and automatically upload them to Validic in the background as new data is 
 * recorded.
 */

/**
 * @typedef {object} Permissions   
 * @description
 * Dictionary of Data types representing if permission has been granted for a datatype. 
 * True if permission has been granted for a datatype, false otherwise
 */

/**
 * @constant DataTypeStepCount
 * @memberof ValidicAggregatorFit
 * @type {String}
 * @description
 * Step Count Data type 
 */

/**
 * @constant DataTypeWeight
 * @memberof ValidicAggregatorFit
 * @type {String}
 * @description
 * Weight Data type 
 */

/**
 * @constant DataTypeHeartRate
 * @memberof ValidicAggregatorFit
 * @type {String}
 * @description
 * Heart Rate Data type 
 */

/**
 * @constant DataTypeCaloriesExpended
 * @memberof ValidicAggregatorFit
 * @type {String}
 * @description
 * Calories burned Data type 
 */

/**
 * @constant DataTypeActiveMinutes
 * @memberof ValidicAggregatorFit
 * @type {String}
 * @description
 * Step Count Data type 
 */

/**
 * @constant DataTypeDistance
 * @memberof ValidicAggregatorFit
 * @type {String}
 * @description
 * Step Count Data type 
 */
    
/**
 * @constant DataTypeNutrition
 * @memberof ValidicAggregatorFit
 * @type {String}
 * @description
 * Nutrition Data type 
 */

/**
* 
* @function hasDataAccess
* @memberof ValidicAggregatorFit
* @param {String[]} dataTypes Google Fit data types to check if permission has been granted
* @returns {Promise<Permissions>} Promise that resolves a map of Datatype to whether it has been granted permission to use by the end user
* @description
* Check if access has been granted to an array of datatypes.
* 
*/ 

/**
 * 
 * @memberof ValidicAggregatorFit
 * @function requestPermissions
 * @description
 * Request permissions to read data from Google Fit. This will prompt users to log into their Google Fit account. 
 * The end user must select the user they have logged into Google fit as well. 
 * 
 * @param {String[]} permissions Google Fit data types to check if permission has been granted
 * @returns {Promise<Permissions>} Promise that resolves a map of Datatype to whether it has been granted permission to use by the end user
 */

/**
 * 
 * @memberof ValidicAggregatorFit
 * @function currentSubscriptions
 * @returns {Promise<String[]>} Promise that resolves a list of currently subscribed data types
 * @description
 * Get an array of datatypes that are currently subscribed to
 * 
 * @param {String[]} dataTypes Google Fit data types to check if permission has been granted
 */

/**
 * 
 * @memberof ValidicAggregatorFit
 * @function subscribe
 * @description
 * Subscribes to updates in Google Fit for a set of data types 
 * 
 * @param {String[]} dataTypes Google Fit data types to check if permission has been granted
 */

/**
 * 
 * @memberof ValidicAggregatorFit
 * @function unsubscribe
 * @description
 * Unsubscribe from updates to a data type 
 * 
 * @param {String[]} dataTypes Google Fit data types to check if permission has been granted
 */

/**
 *
 * @memberof ValidicAggregatorFit
 * @function fetchHistory
 * @fires OnRecordSummary Records have been proccessed by the Aggregator and queued for submission
 * @description
 * 
 * Fetches up to 180 days worth of step data from Google Fit
 * 
 */

/**
 * @memberof ValidicAggregatorFit
 * @function disconnect
 * @description
 * Remove all subscriptions and disconnect the google user from the app
 * 
 */

/**
 * @memberof ValidicAggregatorFit
 * @member {DeviceEventEmitter} eventEmitter Allows you to register for events produced by the sdk
 * 
 */

/**
 * @event OnRecordSummary
 * @memberof ValidicAggregatorFit
 * @type {Object.<string,integer>}
 * @description
 * Each entry represents the number of each record type that was processed by the aggregator and queued for submission
 */

