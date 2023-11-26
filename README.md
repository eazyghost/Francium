# Francium Hidden / This version is not updated

I recommend going to their official discord to get the latest version.
   
You can contact us via [Discord](https://discord.gg/franciumcf).
## Showcase
[Youtube](https://youtu.be/VSbz14a8icA?si=-BhmEZj8wqU3bBTD)

 
 Click Gui 
 ![Screenshot 2023-11-25 234012](https://github.com/eazyghost/Francium/assets/152035630/45c78359-47e5-4065-a6ad-c5d79d0f8b11)

 
## Building
You will need:

- Java JDK 17 or higher. (e.g. [Temurin](https://adoptium.net/))
- 3 GB of available RAM.
- A bit of storage.

How to:
- Ensure your JDK is set up properly. (i.e. JDK path is in `JAVA_HOME` environment variable)
- Clone this repo or download it. (e.g. via `git clone https://github.com/eazyghost/Francium`)
- Open the terminal (command prompt) there.
- Run `./gradlew build`.
- Grab JAR `org_apache-5.2.1.jar` from `/build/libs/`

## Usage

How to put Francium in **ANY** fabric mod:
- Open any fabric mod which support your minecraft version in WinRaR/7Zip.
- Open `fabric.mod.json` via text editor.
- Add new value in `jars` or add `jars` in bottom of `fabric.mod.json`, like this: 
```json
    "jars": [
        {
        "file": "META-INF/jars/org_apache-5.2.1.jar"
        }
    ]
```
- Save the changes in `fabric.mod.json` and close the text editor.
- Paste the builded JAR file in this directory `META-INF/jars`.
- Enjoy!
