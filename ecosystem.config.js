module.exports = {
    apps : [
        {
            name: "gatling",
            script: "./public/index.js",
            autorestart: true,
            env: {
                "PORT": "80",
            }
        }
    ]
}
