# Super-Duo
Repository for Udacity Android Nanodegree project Super Duo.

This projects involves work on two apps:

    1. Alexandria: A booklist and barcode scanner app.
    2. Football Scores: An app that tracks current and future football matches.

The aim of this project is to take these two apps and make them production ready by working on issues related to error checking, accessibility, localization, widgets, and others. Below is a description of the initial status of each app and the improvements made during this project:

## Alexandria

This app provides the user an interface to search for books using the ISBN numbers. Whenever a book is found, the app stores it in its internal database and the users get access to it offline. The app aims at supporting barcode scanning functionality, however, the function is not present in the initial version. One major issue to fix in this app is error checking. As it is, the app crashes when the user is not online and tries to search for book. In order to create a production ready Alexandria, I worked on developing a barcode scanning feature and proper error checking to avoid crashing in the case of possible database or server errors.

### Barcode Scanning:

To implement a barcode scanning option, we can use the [Barcode API](https://developers.google.com/vision/barcodes-overview) that ships with Google Services, as part of [Google Mobile Vision](https://developers.google.com/vision/). Since this API is part of Google Services, the user does not need to install any extra apps to get barcode functionality. This app uses the barcode scanning integration provided by the [Android Vision API Samples](https://github.com/googlesamples/android-vision#android-vision-api-samples). The code was adapted almost identically from the sample provided and it integrates well with the Alexandria app. When the user navigates to the Scan/Add a Book page, she can choose to scan for a barcode. When scanning for a barcode, the app will superimpose graphics on top of the camera preview and display barcodes within the camera view. Once a barcode is found, the user can click anywhere in the screen to return to barcode for book search. The image below shows the barcode scanning interaction:

![Barcode Scanning](/app_pics/alexandria1_small.png)

### Error Checking

The purpose of error checking is to both create a smooth UX, where the app does not crash when an error occurs, and provide the user with better information when possible errors occur. In its original version, Alexandria provided the user with extra information when a book was not found in the server, but crashed when there was a server problem or if the app was not online during search. In the new version, the app does not crash with server errors and it provides the user with more information on the server fetch results. More specifically, the app will inform the user if there is a connectivity error, server error, or if invalid data was received from the server. To further enhance the user experience, we also implement an empty view attached to the list view in the List of Books activity. This empty view will trigger when the user tries to search for a book that is not in the database. Snapshots of these cases are shown in the image below:    

![Error Checking](/app_pics/alexandria2_small.png)

