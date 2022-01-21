import org.junit.Assert;
import org.junit.Test;

import de.aubli.legacychatconverter.Converter;

/**
 * @author AlexMl Created on 02.11.21 for LegacyChatConverter
 */
public class ConverterTest {

    @Test
    public void testSectionConverter() {
        Assert.assertEquals("Conversion wrong",
                "Component.text(\"text here\")",
                Converter.convertSection("\"text here\"")
        );
        Assert.assertEquals("Conversion wrong",
                "Component.text(\"text here\")",
                Converter.convertSection(" \"text here\" ")
        );
        Assert.assertEquals("Conversion wrong",
                "Component.text(\"text hier\", NamedTextColor.RED)",
                Converter.convertSection("ChatColor.RED + \"text hier\"")
        );

        Assert.assertEquals("Conversion wrong",
                "Component.text(\"OOPS I could not send you to \", NamedTextColor.RED).append(Component.text(Core.getServerName(), NamedTextColor.RED)).append(Component.text(\"Please try again!\", NamedTextColor.RED))",
                Converter.convertSection("ChatColor.RED + \"OOPS I could not send you to \" + Core.getServerName() + ChatColor.RED + \"Please try again!\"")
        );

        Assert.assertEquals("Conversion wrong",
                "Component.text(\"OOPS\", NamedTextColor.RED).append(Component.newline()).append(Component.newline()).append(Component.text(\"I could not send you to \", NamedTextColor.RED)).append(Component.text(Core.getServerName(), NamedTextColor.RED)).append(Component.text(\"\", NamedTextColor.RED)).append(Component.newline()).append(Component.text(\"\", NamedTextColor.RED)).append(Component.text(\"Please try again!\", NamedTextColor.RED))",
                Converter.convertSection("ChatColor.RED + \"OOPS\\n\\nI could not send you to \" + Core.getServerName() + \"\\n\" + ChatColor.RED + \"Please try again!\"")
        );
    }

    @Test
    public void testAmpersandConverter() {
        Assert.assertEquals("Conversion wrong", "Component.text(\"No Color here\")", Converter.convertAmpersand("No Color here"));
        Assert.assertEquals("Conversion wrong", "Component.text(\" No Color here \")", Converter.convertAmpersand(" No Color here "));
        Assert.assertEquals("Conversion wrong",
                "Component.text(\"Hello World!\", NamedTextColor.GRAY)",
                Converter.convertAmpersand("ChatColor.translateAlternateColorCodes('&', \"&7Hello World!\")")
        );
        Assert.assertEquals("Conversion wrong",
                "Component.text(\"Hello World!\", NamedTextColor.GRAY)",
                Converter.convertAmpersand(" ChatColor.translateAlternateColorCodes('&', \"&7Hello World!\") ")
        );
        Assert.assertEquals("Conversion wrong",
                "Component.text(\"➥ \", NamedTextColor.GOLD).append(Component.text(\"You must have the Manos, Gyra or Zume rank to display items with \", NamedTextColor.GRAY)).append(Component.text(\"[item]\", NamedTextColor.AQUA)).append(Component.text(\". Upgrade today at \", NamedTextColor.GRAY)).append(Component.text(\"store.mineheroes.net\", Style.style(NamedTextColor.AQUA).decorate(TextDecoration.UNDERLINED)))",
                Converter.convertAmpersand("&6➥ &7You must have the Manos, Gyra or Zume rank to display items with &b[item]&7. Upgrade today at &b&nstore.mineheroes.net")
        );
    }

    @Test
    public void testAmpersandToChatColor() {
        Assert.assertEquals("Conversion wrong",
                "ChatColor.GRAY + \"Hello \" + ChatColor.RED + \"World!\"",
                Converter.convertAmpersandToChatColor("&7Hello &cWorld!")
        );
        Assert.assertEquals("Conversion wrong",
                "ChatColor.GRAY + \"My Friends \" + ChatColor.RED + \"& \" + ChatColor.GRAY + \"I!\"",
                Converter.convertAmpersandToChatColor("&7My Friends &c& &7I!")
        );
        Assert.assertEquals("Conversion wrong",
                "ChatColor.GRAY + \"Hello \" + ChatColor.RED + \"World!\"",
                Converter.convertAmpersandToChatColor("ChatColor.translateAlternateColorCodes('&', \"&7Hello &cWorld!\")")
        );
        Assert.assertEquals("Conversion wrong",
                "ChatColor.GRAY + \"Hello \" + ChatColor.RED + \"World!\"",
                Converter.convertAmpersandToChatColor(" ChatColor.translateAlternateColorCodes('&', \"&7Hello &cWorld!\") ")
        );
        Assert.assertEquals("Conversion wrong",
                "ChatColor.GRAY + ChatColor.UNDERLINE.toString() + \"Hello \" + ChatColor.RED + ChatColor.BOLD.toString() + \"World!\"",
                Converter.convertAmpersandToChatColor("ChatColor.translateAlternateColorCodes('&', \"&7&nHello &c&lWorld!\")")
        );
        Assert.assertEquals("Conversion wrong",
                "ChatColor.GREEN + ChatColor.BOLD.toString() + \"(!) \" + ChatColor.GRAY + \"You have \" + ChatColor.GREEN + \"successfully \" + ChatColor.GRAY + \"applied a \" + cursor.getItemMeta().getDisplayName()",
                Converter.convertAmpersandToChatColor("ChatColor.translateAlternateColorCodes('&', \"&a&l(!) &7You have &asuccessfully &7applied a \" + cursor.getItemMeta().getDisplayName())"));
        Assert.assertEquals("Conversion wrong",
                "\"A Thank You wave has started because \" + ChatColor.GOLD + ChatColor.BOLD.toString() + \"\" + player.getPlayerName() + \" \" + ChatColor.WHITE + \"made a purchase on \" + ChatColor.GREEN + ChatColor.BOLD.toString() + \"store.mineheroes.net\"",
                Converter.convertAmpersandToChatColor("ChatColor.translateAlternateColorCodes('&', \"A Thank You wave has started because &6&l\" + player.getPlayerName() + \" &fmade a purchase on &a&lstore.mineheroes.net\")")
        );
        Assert.assertEquals("Conversion wrong",
                "ChatColor.GRAY + \"Type a new name in chat (color code guide: \" + ChatColor.AQUA + ChatColor.UNDERLINE.toString() + \"ess.khhq.net/mc/\" + ChatColor.GRAY + \")\"",
                Converter.convertAmpersandToChatColor("ChatColor.translateAlternateColorCodes('&', \"&7Type a new name in chat (color code guide: &b&ness.khhq.net/mc/&7)\")"));
    }

}
