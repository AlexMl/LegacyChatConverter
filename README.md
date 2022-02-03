# LegacyChatConverter

[![Build](https://github.com/AlexMl/LegacyChatConverterPlugin/actions/workflows/build.yml/badge.svg)](https://github.com/AlexMl/LegacyChatConverterPlugin/actions/workflows/build.yml)

<!-- Plugin description -->
This Fancy IntelliJ Platform Plugin will help you convert legacy chat, like
- <kbd>ChatColor.RED + "Hello" + ChatColor.AQUA + " World!"</kbd> 
- <kbd>ChatColor.translateAlternateColorCodes('&', "&cHello&b World!")</kbd>

into new modern adventure Components:
- <kbd>Component.text("Hello", NamedTextColor.RED).append(Component.text(" World!", NamedTextColor.AQUA))</kbd>

Just select the message, right click and select <i>Convert to Component</i>.

<!-- Plugin description end -->

## Installation

- Manually:

  Download the [latest release](https://github.com/AlexMl/LegacyChatConverterPlugin/releases/latest) and install it
  manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>

---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template
