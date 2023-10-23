FROM node:18.18-alpine

WORKDIR /app

# Copying necessary files.
COPY . .

ENV NODE_OPTIONS --openssl-legacy-provider

RUN export NODE_OPTIONS=--openssl-legacy-provider

RUN npm install -g @angular/cli
RUN npm install

RUN npm run build --prod

# Command we want to run when our image is executed inside a container
CMD ["ng", "serve"]
