# react-native-validic-aggregator-fit

This project integrates Google Fit with the Validic Mobile Aggregator to passively collect data on behalf of an end user. Validic ensures that our Mobile SDK makes use of the minimal scopes needed for our supported data types. Additionally, Validic does not request or use any scopes indicated as sensitive or restricted by Google. Avoiding sensitive and restricted scopes ensures that you will not need to complete Google's sensitive scope verification or restricted scope verification and security assessment - both of which are time-consuming and costly.

Be aware that Google has an extensive and potentially lengthy verification process. Google manages their own verification process and it is subject to change at any time at their discretion. It is your responsiblity to review, understand, and follow Google's verification process and provide Google with all information they request before sharing your application publicly. Validic does not provide direct support for Google's verification process, however, [Google's Verification FAQ](https://support.google.com/cloud/answer/9110914?hl=en) is detailed and provides you with the information you'll need to complete the process. See the [Customer Requirements](#customer-requirements) section below for highlights on setting up, developing, and verifying for Google Fit.

## Customer Requirements
Google Fit is available for Inform (V2) Validic customers. If you are a Legacy customer and are interested in Google Fit, please reach out to your Validic Client Success Manager.

Validic's Google Fit integration works with Google Fit for Android. Google Fit for Android uses Google's OAuth API for accessing user data. There is setup that must occur before you implement Google Fit into your mobile project so that your users can consent to sharing and share their data successfully:

Before you start development, in [Google Api Console](https://console.developers.google.com) you must:

* [Create Google Services OAuth 2.0 Client IDs](https://developers.google.com/fit/android/get-api-key)
* [Verify your domain ownership](https://support.google.com/cloud/answer/9110914?hl=en#how-smooth)
* [Configure your user consent screen](https://support.google.com/cloud/answer/6158849#userconsent)

After completing the above setup, you can start development using the instructions in this tutorial. While you are developing, testing can occur with Google user accounts that are registered to the same domain as the mobile project.

After development and testing are complete and before you launch your app, you will need to submit a request to Google for Oauth API Verification. There are certain instances where this may not be required (see [Google's FAQ](https://support.google.com/cloud/answer/9110914?hl=en#skip) for details).

Once you submit your verification request, you may be required to provide some or all of the following:

* Application Homepage detailing your use of requested scopes
* Privacy Policy and Terms & Conditions
* Application Demo Video showing the user consent workflow in your app
* Application Name, Logo, and/or other branding elements

Google dictates their own verification process so you may be asked to provide additional information as well. See  [Veification Requirements](https://support.google.com/cloud/answer/9110914?hl=en#verification-requirements) for more information. Be sure to respond to Google's requests as quickly as possible to minimize delays in getting your verification approved.


## Getting started

`$ npm install react-native-validic-aggregator-fit --save`

### Mostly automatic installation

`$ react-native link react-native-validic-aggregator-fit`

## Usage

```javascript
import ValidicAggregatorFit from 'react-native-validic-aggregator-fit';
```

### Initialization

`ValidicAggregatorFit` will be initialized at app startup and will immediately start listening for 
previously subscribed data types. It is recommended that you set a listener on the `ValidicAggregatorFit` during your `componentDidMount()` method 

``` javascript
    componentDidMount() {
        ValidicAggregatorFit.eventEmitter.addListener('validic:fit:onrecords',(summary)=>{
            console.log("Proccessed Google Fit Data");
            console.log(summary);
        });
        ValidicAggregatorFit.eventEmitter.addListener('validic:fit:onerror', (callback)=>{
            console.warn(callback.error);
        });
```

Make sure to unsubscribe from events during `componentWillUnmount()`. 

``` javascript
    componentWillUnmount() {
        ValidicAggregatorFit.eventEmitter.removeAllListeners('validic:fit:onrecords');
        ValidicAggregatorFit.eventEmitter.removeAllListeners('validic:fit:onerror');
```

### Requesting Permissions

Before collecting any data from Google Fit, a Google Account must be associated with the `ValidicAggregatorFit`, and the end user must authorize the app to collect data on their behalf.

#### Previously granted permissions
To check if permissions have already been granted the `hasDataAccess(dataTypes)` method can be used

``` javascript
    let permissions = await ValidicAggregatorFit.requestPermissions([ValidicAggregatorFit.DataTypeStepCount])
    if(permissions[ValidicAggregatorFit.DataTypeStepCount] == true){
        //subscribe
    }
```
The promise returned from `hasDataAccess(dataTypes)` associates a `DataType` with true, if a valid Google account exists and permission has previsouly been granted, or false otherwise.

#### Requesting new permissions

To request permission from the end user to authorize data collection the `requestPermissions(dataType)` function can be used to authorize your application

``` javascript
 let permissions = await ValidicAggregatorFit.requestPermissions([ValidicAggregatorFit.DataTypeStepCount])
 if(permissions[ValidicAggregatorFit.DataTypeStepCount] == true){
     //subscribe
 }

``` 

### Subscriptions
After Permissions have been granted for a `DataType` the `ValidicAggregatorFit` can be used to add subscriptions to data change events from Google Fit. 
Data will automatically will queued for submission to the Validic API when internet is available on the device

``` javascript
    ValidicAggregatorFit.subscribe([ValidicAggregatorFit.DataTypeStepCount]);
```

To unregister from receiving data update events use the `ValidicAggregatorFit` to unsubscribe

``` javascript
    ValidicAggregatorFit.unsubscribe(ValidicAggregatorFit.DataTypeStepCount);
```

Get a list of currently subscribed `DataType`s 

``` javascript
    let subscriptions = await ValidicAggregatorFit.currentSubscriptions()
```

#### Currently Supported DataTypes
Available datatypes can be referenced from the `ValidicAggregatorFit`._datatype_

* DataTypeStepCount
* DataTypeWeight
* DataTypeHeartRate
* DataTypeCaloriesExpended
* DataTypeMoveMinutes
* DataTypeDistance
* DataTypeNutrition

Records collected are automatically submitted to the Validic Inform API and a summary of records processed will be sent as an event named `validic:fit:onrecords`.

For an example of setting a listener see [Initialization](#initialization)

### Logout
To remove all subscriptions and log an end user out simply call

``` javascript
    ValidicAggregatorFit.disconnect()
```



