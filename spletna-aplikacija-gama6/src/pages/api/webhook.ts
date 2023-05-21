// pages/api/webhooks/github.js
import { exec } from "child_process";
import crypto from "crypto";
/*eslint-disable */

export default function handler(req, res) {
  try {
    console.log("Incoming Request");
    if (req.method !== "POST") {
      res.send(404);
      return;
    }
    let sig =
      "sha256=" +
      crypto.createHmac("sha256", process.env.WEBHOOKS_SECRET)
        .update(JSON.stringify(req.body))
        .digest("hex");
    if (
      req.headers["x-hub-signature-256"] === sig &&
      req.body?.ref === "refs/heads/main" &&
      process.env.REPO_PATH
    ) {
      exec(
        "cd " +
          process.env.REPO_PATH +
          " && git pull && npm install && npm run build && pm2 restart app"
      );
      console.log("GitHub Webhook ran successfully");
      res.end();
      return;
    }
    console.log("GitHub Webhook failed");
    res.end();
    return;
  } catch (e) {
    console.log(e);
  }
}
