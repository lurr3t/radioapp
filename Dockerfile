
FROM ubuntu:latest

# Install required packages
RUN apt-get update && apt-get install -y

# Set the DISPLAY environment variable
ENV DISPLAY=:0

# Install java
RUN apt install -y default-jre
RUN apt install -y default-jdk


# Set the working directory in the container
WORKDIR /usr/src/app

# Copy the current directory contents into the container at /usr/src/app
COPY . .

RUN cd src && javac Main/Radio.java

# Set the working directory to src/Main
WORKDIR /usr/src/app/src

# Run the Java program
CMD ["java", "Main/Radio"]