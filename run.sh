docker build -t radio . && docker run -it --rm --name radio \
--net=host -e DISPLAY=$DISPLAY -v /tmp/.X11-unix:/tmp/.X11-unix radio