# Super-Duo
Repository for Udacity Android Nanodegree project Super Duo.

This projects involves work on two apps:

    1. Alexandria: A booklist and barcode scanner app.
    2. Football Scores: An app that tracks current and future football matches.

The aim of this project is to take these two apps and make them production ready by working on issues related to error checking, accessibility, localization, widgets, and others. Below is a description of the initial status of each app and the improvements made during this project:

## Alexandria

This app provides the user an interface to search for books using the ISBN numbers. Whenever a book is found, the app stores it in its internal database and the users get access to it offline. The app aims at supporting barcode scanning functionality, however, the function is not present in the initial version. One major issue to fix in this app is error checking. As it is, the app crashes when the user is not online and tries to search for book. In order to create a production ready Alexandria, I worked on developing a barcode scanning feature and proper error checking to avoid crashing in the case of possible database or server errors.

### Barcode Scanning:

To implement a barcode scanning option, we can use the [Barcode API](https://developers.google.com/vision/barcodes-overview) that ships with Google Services, as part of [Google Mobile Vision](https://developers.google.com/vision/). Since this API is part of Google Services, the user does not need to install any extra apps to get barcode functionality. This app uses the barcode scanning integration provided by the [Android Vision API Samples](https://github.com/googlesamples/android-vision#android-vision-api-samples). The code was adapted almost identically from the sample provided and it integrates well with the Alexandria app. When the user navigates to the Scan/Add a Book page, she can choose to scan for a barcode, which will send her to the page below:

![Barcode Scanning](/app_pics/barcode_scanning.png)      
