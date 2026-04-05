# Build Angular (production static browser bundle)
FROM node:22-alpine AS build
WORKDIR /app
COPY package.json package-lock.json ./
RUN npm ci
COPY . .
ENV NODE_OPTIONS=--max-old-space-size=4096
RUN npm run build:docker

# Serve static SPA (no nginx): `serve` handles client-side routing via -s
FROM node:22-alpine
WORKDIR /app
RUN npm install -g serve@14
COPY --from=build /app/dist/travel-destination-planner/browser ./public
EXPOSE 4200
CMD ["serve", "-s", "public", "-l", "4200"]
