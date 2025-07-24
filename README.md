# AutoAdvertising

A plugin for automatic advertising on Minecraft servers (Spigot/Paper 1.21.8+).

## Features
- Scheduled automatic ad messages
- Gradient color support
- Full command and permission system
- Easy configuration

## Installation
1. Download the latest release from [Modrinth](https://modrinth.com/plugin/autoadvertising).
2. Place the jar file into your `plugins` folder.
3. Start or reload your server.

## Configuration
Edit `plugins/AutoAdvertising/config.yml` to add your ads and set the interval.

```
interval: 60
ads: {}
```

## Commands
- `/autoad reload` — Reload config
- `/autoad createad <id>` — Create ad
- `/autoad deletead <id>` — Delete ad
- `/autoad addline <id> <line>` — Add line to ad
- `/autoad removeline <id> <index>` — Remove line from ad
- `/autoad setinterval <seconds>` — Set interval
- `/autoad gradient <#from> <#to> <text>` — Show gradient example
- `/autoad help` — Show help

## Permissions
- `autoadvertising.admin` — Full access
- `autoadvertising.reload` — Reload config
- `autoadvertising.manage.ads` — Manage ads
- `autoadvertising.manage.lines` — Manage lines
- `autoadvertising.manage.interval` — Change interval

## License
MIT

---
[Modrinth Project Page](https://modrinth.com/plugin/autoadvertising) 