Android Dev Summit 2022 Speed Challenge
==================

Note: This repository was used for the ADS and is not being maintained. As of Oct 2022 it works on Dolphin..Flamingo and uses AGP 7.3.0.

# Instructions for contestants

1. Clone repository

2. Start coding!
Check the local tests in `test/`. Do not modify the tests!

3. Run tests locally
Make all tests in `app/test/java` pass.

4. Do not send PRs to the repo!

# Instructions for hosts

1. Create a couple of slides explaining the goal of the challenge and showing a screenshot of the app.
1. Show the first hint on screen and drop a new one every ~5 minutes.
2. Make sure contestants don't modify the tests instead of the code under test!

## Hints

### Test order
1. `LocationScreenTest`
1. `LocationViewModelTest`
1. `UiLayerTest`
1. `LocationConfigChangeTest`

### LocationScreenTest
* Fix hardcoded title in `TopAppBar`
* Swap IDs in left/right icons

### LocationViewModelTest
* Catch error in `LocationViewModel`'s Flow (don't forget to emit it)

### UiLayerTest
* `LocationScreenForecast`: iterate on `forecast.forecastWeek` and create multiple rows in a column.

### LocationConfigChangeTest
* `expandedDays` should be saveable (look for `indexSaver`!)


# License

Now in Android is distributed under the terms of the Apache License (Version 2.0). See the
[license](LICENSE) for more information.
