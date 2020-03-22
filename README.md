## Dancer

To start the dancer project locally, use the following command
in the projects main directory:

```
./gradlew clean build
./gradlew bootRun
```

This will start both backend and frontend, you can access the landing page via http://localhost:8080
and the dancer detail page via http://localhost:8080/dancers

To have the frontend in development mode (including hot reload) use the following command in the frontend
subfolder:

```
npm start
``` 

You can access the frontend via http://localhost:3000/