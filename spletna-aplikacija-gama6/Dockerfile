FROM node:18-alpine

WORKDIR /app

COPY prisma ./

COPY package*.json ./

RUN npm install

COPY . .

ENV NODE_ENV=development
ENV NODE_ENV development
ENV DATABASE_URL=file:./db.sqlite
ENV NEXTAUTH_URL=http://localhost:3000
ENV DISCORD_CLIENT_ID=
ENV DISCORD_CLIENT_SECRET=

EXPOSE 3000

CMD ["npm", "run", "dev"]
