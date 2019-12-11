# SmartConverter

SmartConverter Application:

    The application converts the currencies for the rates provided by the end point.

    Application uses following technologies:

    1) Dagger2
    2) LiveData
    3) RxJava
    4) Retrofit 2
    5) Mockito for UnitTesting
    6) MVP (Model-View-Presenter)

    When user launches the application firstly default currency is EURO and then
    User can change the currency depending on his/her interest and then can change
    the amount and list will be automatically updated every second.

    MVP design structure used and Presenter is responsible to perform
    business logic of the application such as making HTTP calls and updating the
    base and amount.

    The presenter unit tests are included inside the ConverterPresenterTest class
    Every single scenario has been tested.

    If ConverterFragment is destroyed or stopped the Rxjava disposable queue is
    cleared and on onResume of the fragment the services is triggered again.

    Application handles the network errors.