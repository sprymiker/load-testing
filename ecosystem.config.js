module.exports = {
    apps : [
        {
            name: "gatling",
            script: "./public/index.js",
            autorestart: true,
            env: {
                PORT: "80",
                JAVA_OPTS: (process.env.JAVA_OPTS || '')
                + ` -Xms4096m`        //set initial Java heap size
                + ` -Xmx8192m`        //set maximum Java heap size
            }
        }
    ]
}
