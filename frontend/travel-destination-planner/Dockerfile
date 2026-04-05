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
# Angular static build often emits index.csr.html; `serve` needs index.html as the SPA entry.
RUN if [ -f /app/public/index.html ]; then true; \
    elif [ -f /app/public/index.csr.html ]; then cp /app/public/index.csr.html /app/public/index.html; \
    else echo "No index.html or index.csr.html under public/" >&2; exit 1; fi
EXPOSE 4200
CMD ["serve", "-s", "public", "-l", "4200"]
