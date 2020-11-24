# node build
FROM node:10-alpine as builder

COPY package.json package-lock.json ./
RUN npm install && mkdir /front-end && mv ./node_modules ./front-end
WORKDIR /front-end
COPY . .
RUN npm run ng build

# nginx
FROM nginx:alpine
MAINTAINER galaksiya

RUN rm -rf /etc/nginx/conf.d/default.conf
COPY ./default.conf /etc/nginx/conf.d/default.conf
COPY --from=builder /front-end/dist/personality-web /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
