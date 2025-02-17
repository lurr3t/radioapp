install 
java sudo apt install default-jre
javac sudo apt install default-jdk


# is probably not needed
sudo apt-get install xvfb
Xvfb :99 -screen 0 1024x768x16 &
export DISPLAY=:99
java -cp out Main.Radio

