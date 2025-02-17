# Radio Schedule Application

## Overview

This application is a radio schedule viewer that fetches data from the SverigesRadio API. It displays the schedule of various radio channels in a user-friendly GUI.

## Features

- Displays radio schedules in a table format.
- Allows users to refresh the schedule data.
- Provides detailed information about each episode in a popup window.
- Categorizes channels for easy navigation.

## Prerequisites

- Docker must be installed on your system.

## How to Run

1. **Clone the repository:**
	```sh
	git clone <repository-url>
	cd radioapp
	```

2. **Build and run the application using Docker:**
	```sh
	./run.sh
	```

This script will build the Docker image and run the container, setting up the necessary environment to execute the Java application.
