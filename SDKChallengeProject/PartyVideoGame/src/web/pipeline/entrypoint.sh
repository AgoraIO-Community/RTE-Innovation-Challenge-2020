#!/bin/sh
if [ -z "$REACT_APP_APP_ID" ]
then
    echo "\$REACT_APP_APP_ID is empty"
else
    find ./ -type f -exec sed -i -e "s/{REACT_APP_APP_ID_PLACEHOLDER}/$REACT_APP_APP_ID/g" {} \;
fi

if [ -z "$REACT_APP_FIREBASE_KEY" ]
then
    echo "\$REACT_APP_FIREBASE_KEY is empty"
else
    find ./ -type f -exec sed -i -e "s/{REACT_APP_FIREBASE_KEY_PLACEHOLDER}/$REACT_APP_FIREBASE_KEY/g" {} \;
fi

if [ -z "$REACT_APP_FIREBASE_PROJECT_ID" ]
then
    echo "\$REACT_APP_FIREBASE_PROJECT_ID is empty"
else
    find ./ -type f -exec sed -i -e "s/{REACT_APP_FIREBASE_PROJECT_ID_PLACEHOLDER}/$REACT_APP_FIREBASE_PROJECT_ID/g" {} \;
fi

if [ -z "$REACT_APP_FIREBASE_APP_ID" ]
then
    echo "\$REACT_APP_FIREBASE_APP_ID is empty"
else
    find ./ -type f -exec sed -i -e "s/{REACT_APP_FIREBASE_APP_ID_PLACEHOLDER}/$REACT_APP_FIREBASE_APP_ID/g" {} \;
fi

if [ -z "$REACT_APP_FIREBASE_SENDER_ID" ]
then
    echo "\$REACT_APP_FIREBASE_SENDER_ID is empty"
else
    find ./ -type f -exec sed -i -e "s/{REACT_APP_FIREBASE_SENDER_ID_PLACEHOLDER}/$REACT_APP_FIREBASE_SENDER_ID/g" {} \;
fi

if [ -z "$REACT_APP_FIREBASE_MEASUREMENT_ID" ]
then
    echo "\$REACT_APP_FIREBASE_MEASUREMENT_ID is empty"
else
    find ./ -type f -exec sed -i -e "s/{REACT_APP_FIREBASE_MEASUREMENT_ID_PLACEHOLDER}/$REACT_APP_FIREBASE_MEASUREMENT_ID/g" {} \;
fi

nginx -g 'daemon off;'