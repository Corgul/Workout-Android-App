# Workout Log App

## About

Tired of using the notes app or pen and paper to log workouts? Look no further because with this app you can log any number of workouts
that you desire. Unlike competitor applications that lock features behind paywalls, this app is completely free.

## Features

### Calendar Page

The main page of the app is the calendar page. This page allows you to access any day's workouts. If the day on the calendar has a blue dot underneath it
that means it has an existing workout. You can add a workout using the floating action `+` button. This page also features an expandable bottom sheet displaying the
summary for the workout.

[!Calendar](/assets/calendar.png)
[!Calendar Bottom Sheet](/assets/calendar-bottom-sheet.png)

### Edit Workout Page

The edit workout page allows you to edit existing workouts as well as add new ones. You can go to this page from the bottom navigation bar, floating action button,
or calendar bottom sheet. Some features on this page include
* Add exercises for different muscle groups
* Add, delete, reorder sets for a particular exercise
* Edit the workout name or delete it
* Reorder the order that the exercises come in

[!Workout Page](/assets/workout.png)
[!Workout Bottom Sheet](/assets/workout-bottom-sheet.png)
[!Reorder Exercise](/assets/reorder-exercise.png)

## Implementation

* This app is built using `Compose` and clean architecture best practices
* A `Room` Database helps keep track of all data to ensure persistence
  * There are two databases, one that represents your workouts and another that represents Exercises and their muscle group types
  * The workout database has entities `Workout`, `Exercise`, and `ExerciseSet`
  * The exercise type database gets its initial values from `exercise_name.json` and `exercise_type.json`
* `Hilt` is used for dependency injection