echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin
docker build -t sem56402018/fitbit-ingest:$1 -t sem56402018/fitbit-ingest:$TRAVIS_COMMIT .
docker push sem56402018/fitbit-ingest:$TRAVIS_COMMIT
docker push sem56402018/fitbit-ingest:$1
