#!/bin/sh
ps uax | grep java | grep target | awk '//{print $2}' | xargs -i kill {}
