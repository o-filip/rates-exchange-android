# RatesExchange

## Project overview

The Currency Exchange Rates App is an example Android application designed to demonstrate practices for architecture, code style, and various features and can be used as a template for other projects. Even though the app itself is relatively simple, the architecture of the project has been designed to showcase a medium-scale project structure. This approach allows for a demonstration of how the architecture can scale and accommodate more complex features.

It provides users with up-to-date currency exchange rates obtained from the [Currency beacon API](https://currencybeacon.com/), allowing them to convert between different currencies. The app also incorporates offline functionality to ensure a seamless user experience even without an internet connection.

### Key Features:

- **Currency Exchange Rates**: The app fetches current currency exchange rates from the [Currency beacon API](https://currencybeacon.com/)  and displays them in a user-friendly list format.
- **Currency Conversion**: Users can convert between different currencies using the exchange rates provided.
- **Offline Mode**: The app offers offline functionality using local database, enabling users to access previously fetched exchange rates and perform currency conversions without an internet connection.
- **Rates Time Series**: The app displays the time series of exchange rates for a selected currency pair, allowing users to track the rate changes over time.
- **Request Rate Limiting**: Used API limits the number of request per day for non-paying users. To  users do not exceed the API request limit and in the interest of example purposes, the app restricts the frequency of data refreshes, allowing users to utilize cached data.
- **Streamlined User Interface**: While the primary emphasis of this example app is on showcasing architecture and code style, it also offers a streamlined user interface. The app's UI design is intentionally kept simple and intuitive, prioritizing a seamless user experience.

## Installation
### Step 1: Create local.properties

In the root directory of the project, create a file named `local.properties` (if it doesn't already exist). This file should contain the necessary configuration for the app to function correctly. You can use the provided `local.properties.example` file as a reference for the required format.

Ensure that you include the following information in the `local.properties` file:
```
// Currency beacon API key. https://currencybeacon.com/
currencyBeacon.apiKey=<your-api-key> 
```

Testing API key can be used: `9f8c8014d1fdb81c07bc33e55a7bd3ac`. In case the request limit for this key is exceeded you can obtain your own API key after free registration on [CurrencyBeacon API](https://currencybeacon.com/).

### Step 2: Create signing.properties (optional)

In the root directory of the project, create a file named `signing.properties`. This file is used for signing the app during the build process. You can use the provided `signing.properties.example` file as a reference for the required format.

Ensure that you include the following information in the `signing.properties` file:

```
# Debug Key
debug.keyAlias=<debug key alias>  
debug.keyPassword=<debug key password>  
debug.storeFile=<path to debug keystore>  
debug.storePassword=<debug keystore password>  

# Release Key
release.keyAlias=<release key alias>  
release.keyPassword=<release key password>  
release.storeFile=<path to release keystore>  
release.storePassword=<release keystore password>
```

Replace values within diamond braces `<>` with the corresponding values for your debug and release keystores.

If the file is not present, the app will be built using the default debug signing configuration.

Please note that it's important to keep the signing properties file secure and avoid sharing it publicly.

### Step 3: Build and Run

After completing the above steps, you should be ready to build and run the Android app. Use your preferred method to compile and launch the application, such as using Android Studio or the command line.

If you encounter any issues during the installation process, please refer to the project documentation or seek assistance from the project maintainers.

## Project Architecture

The app follows a clean architecture pattern in combination with MVVM  and utilizes the Jetpack libraries, with a specific focus on Jetpack Compose for the UI layer. The architecture is designed to incorporate up-to-date technologies and best practices provided by the Android Jetpack components.

The app is structured into three main layers: data, domain, and UI.

- **Data Layer** - The data layer is responsible for handling data communication with the API, storing data into a local database, and providing access points to this layer through repositories.

- **Domain Layer** -  The domain layer contains the business logic of the application. It encompasses a set of use cases and workers that define how the app interacts with data and executes tasks.

- **UI Layer** - The UI layer is responsible for presenting the user interface using Jetpack Compose. It includes the UI components, views, and view models.

Communication between the layers is structured in a one-way manner, following a unidirectional data flow. User interactions in the UI layer are propagated to the domain layer, which processes the requests and communicates with the data layer for data retrieval or modification. Once the data is updated, the UI layer receives the updated data and reflects the changes in the user interface.

## Used technologies, libraries

-   [Compose](https://developer.android.com/jetpack/compose): Used for building the UI in the app.
-   [Compose Navigation](https://developer.android.com/jetpack/compose/navigation): Used for navigating between different screens and managing navigation flow in the app.
-   [MVVM](https://developer.android.com/jetpack/guide): The MVVM architecture pattern is employed for handling UI-related logic in the app.
-   [Coroutines](https://developer.android.com/kotlin/coroutines): Used to achieve app reactivity, update the UI based on data changes, and handle asynchronous tasks efficiently.
-   [Room](https://developer.android.com/jetpack/androidx/releases/room): Used for storing the retrieved currency exchange rates and enabling offline mode functionality in the app.
-   [Work Manager](https://developer.android.com/topic/libraries/architecture/workmanager): Used for performing periodic updates of the currency exchange rates fetched from the API.
-   [Retrofit](https://square.github.io/retrofit/): Used for handling API communication.
-   [Accompanist](https://github.com/google/accompanist): Collection of UI-related libraries that enhance the user interface and provide additional functionalities in the app.
-   [Hilt](https://developer.android.com/training/dependency-injection/hilt-android): Used for dependency injection.
-   [Datastore](https://developer.android.com/topic/libraries/architecture/datastore): Used for storing simple data such as the last selected currencies for conversion and the last update time, ensuring persistence of this data across app sessions.

Additionally, the project also utilizes the following libraries for logging, testing, and mocking:

-   [Timber](https://github.com/JakeWharton/timber): Used for efficient and convenient logging during app development.
-   [JUnit](https://junit.org/junit5/): Used for writing unit tests to ensure the correctness of app components and functionalities.
-   [Mockito](https://site.mockito.org/): Used for creating mock objects and performing mocking in unit tests to isolate dependencies.

## Project packages structure
Project packages structure is divided into 4 main packages. 3 of them (`data`, `domain`, `UI`) mirror the Clean Architecture division described in Project architecture section. `core` package contains classes used across multiple layers.
```
core
  ├── di                    # Dependency injection (Hilt)
  ├── entity                # Data classes representing entities within the project
  ├── error                 # All errors (exceptions) used in the project
  └── extensions            # Kotlin extensions for classes
data
  ├── local                 # Contains classes for managing and retrieving data from local storage
  │   ├── dataStore         # Data stores as an abstraction to access locally stored data
  │   ├── protoStore        # Concrete implementation of Proto Store
  │   └── room              # Concrete implementation of Room
  ├── remote                # Contains classes for managing and retrieving data from remote storage (e.g. API)
  │   ├── dataStore         # Data stores as an abstraction to access remotely stored data
  │   ├── model             # Remote models (format of API response)
  │   └── retrofit          # Concrete implementation of Retrofit
  ├── repository            # Repositories as an access point to the data layer and abstraction from concrete technologies used for storing data
  └── util                  # Data-related utility classes and helpers
domain
  ├── useCase               # Use cases
  └── worker                # Workers for background and periodic tasks
ui
  ├── component             # Reusable UI components used across the app
  ├── navigation            # Navigation between screens
  ├── screen                # Views and ViewModels of screens
  ├── theme                 # Theme
  └── util                  # UI-related utility classes and helpers

```
## Testing

The app includes unit tests to ensure the correctness of the application's logic and behavior. The tests are written using JUnit and Mockito.

### Unit Tests

Easiest way to run test is using Android Studio.

To run the unit tests, follow these steps:

1. Ensure that the project is set up correctly and all dependencies are resolved.

2. Right-click on the desired unit test file or package.

3. Select "Run Tests" or a similar option to execute the unit tests.

4. Monitor the test execution results to check for any failures or errors.
