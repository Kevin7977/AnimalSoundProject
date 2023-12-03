# AnimalSoundProject

## Introduction

The Animal Sound Identifier app leverages Google's voice recognition to analyze recorded animal sounds. Users can either create an account to personalize their experience or log in as a guest.

## Features

- Record animal sounds
- Convert voice to text using Google's voice recognition
- Identify animals based on the recorded sounds
- Display the name and picture of the identified animal
- User authentication via Firebase Authentication
- Store user accounts and animal sound data in Firebase Realtime Database

## Installation

To install the app, follow these steps:

1. Clone the repository
2. Open the project in Android Studio
3. Connect Android Studio with an actual android device that can accese intenet and record sounds
4. Build and run the app on the device


## Usage

1. Launch the app on your device.
2. Register an account if needed
3. Log in with your account or choose the guest option
4. Press the record button to lauch google's voice to text activity
5. The app will convert the sound to text and identify the corresponding animal
6. View the name and picture of the identified animal
7. Press the edit profile button to modify user account, re-enter old password to modify the data

## Dependencies

The Animal Sound Identifier app relies on the following dependencies:

- Google's Voice Recognition API
- Firebase Authentication
- Firebase Realtime Database

## Configuration

### Firebase Configuration

1. Create a Firebase project on the [Firebase Console](https://console.firebase.google.com/).
2. Configure Firebase Authentication and Realtime Database for your project.
3. Change corresponding java code for firebase.
4. Replace the `google-services.json` file in the app module with your project's configuration.
