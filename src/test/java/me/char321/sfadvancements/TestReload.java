package me.char321.sfadvancements;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import org.bukkit.Bukkit;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

class TestReload {

    private static ServerMock server;

    @BeforeAll
    public static void load() {
        server = MockBukkit.mock();
        MockBukkit.load(Slimefun.class);
        MockBukkit.load(SFAdvancements.class);
        server.getScheduler().performOneTick();
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("Test that reloading loads new advancements in the config")
    void testReloadNew() throws IOException {
        //slimefun doesn't load items in a testing environment :NOOOO:
        int oldamount = SFAdvancements.getRegistry().getAdvancements().size();
        File file = server.getPluginManager().getPlugin("SFAdvancements").getDataFolder();
        File advancementsFile = new File(file, "advancements.yml");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(advancementsFile, true))) {
            writer.write("\n" +
                    "test_advancement:\n" +
                    "  group: basic\n" +
                    "  display: DIAMOND\n" +
                    "  name: \"&a[Test Advancement]\"\n" +
                    "  criteria:\n" +
                    "    diamond:\n" +
                    "      name: Obtain a diamond\n" +
                    "      type: inventory\n" +
                    "      item: DIAMOND\n");
        }
        SFAdvancements.instance().reload();
        int newamount = SFAdvancements.getRegistry().getAdvancements().size();
        Assertions.assertEquals(oldamount + 1, newamount);
    }

}
