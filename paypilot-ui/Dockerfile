FROM node:20 AS build
WORKDIR /app
COPY paypilot-ui/package*.json ./
RUN npm install
COPY paypilot-ui ./
RUN npm run build

FROM nginx:1.27-alpine
COPY paypilot-ui/nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /app/dist/paypilot-ui/browser /usr/share/nginx/html
EXPOSE 80
