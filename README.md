## personality-test

Example personality test project which includes a backend-api for saving answers and an angular front-end for simply displaying the questions and selecting answers.
Home page intro texts and steps are copied from [here](http://www.humanmetrics.com/cgi-win/jtypes2.asp).

### Build Steps
1. Clone the project into local environment
1. Change directory into personality-api and execute `mvn clean install -DskipTests=true -P dev,build` to build
1. Execute `docker build -t personality-api .` in the same directory to create docker image
1. Execute `docker compose up -d` to run built backend-api along with postgres database. Postgres will import necessary data on initialization.
1. Change directory into personality-web and execute `docker build -t personality-web .` to build frontend image.
1. Execute `docker compose up -d` in the same directory to run frontend service.
1. Visit http://localhost:8000 to view home page and you can click 'Begin the Test' to fill the answers from selected category.
