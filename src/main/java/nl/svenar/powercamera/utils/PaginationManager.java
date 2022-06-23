package nl.svenar.powercamera.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import nl.svenar.powercamera.PowerCamera;

public class PaginationManager {

    private final ArrayList<String> pageItems;
    private final String pageTitle;
    private final String pageCommand;
    private int currentPage;
    private final int numItemsOnPage;
    private String headerMessage;

    public PaginationManager(List<String> pageItems, String pageTitle, String pageCommand, int currentPage,
                             int numItemsOnPage) {
        this.pageItems = new ArrayList<>(pageItems);
        this.pageTitle = pageTitle;
        this.pageCommand = pageCommand;
        this.currentPage = currentPage + 1;
        this.numItemsOnPage = numItemsOnPage;
    }

    public void setHeaderMessage(String headerMessage) {
        this.headerMessage = headerMessage;
    }

    public void send(CommandSender sender) {
        sender.sendMessage(PowerCamera.getInstance().getCommandHeader(this.pageTitle));

        if (headerMessage != null && headerMessage.length() > 0) {
            sender.sendMessage(headerMessage);
            sender.sendMessage("");
        }

        int totalPages = (int) Math.ceil(pageItems.size() / (float) numItemsOnPage);
        currentPage = Math.max(currentPage, 1);
        currentPage = Math.min(currentPage, totalPages);

        int index = 0;
        for (String item : pageItems) {
            if (index > (currentPage - 1) * numItemsOnPage - 1
                    && index < (currentPage - 1) * numItemsOnPage + numItemsOnPage) {
                sender.sendMessage(item);
            }
            index++;
        }

        String previousPageCommand = ChatColor.BLUE + "[" + ChatColor.AQUA + pageCommand + " "
                + (Math.max(currentPage - 1, 0)) + ChatColor.BLUE + "]";
        String nextPageCommand = ChatColor.BLUE + "[" + ChatColor.AQUA + pageCommand + " "
                + (Math.min(currentPage + 1, totalPages)) + ChatColor.BLUE + "]";
        String currentPageInfo = ChatColor.AQUA + String.valueOf(currentPage) + ChatColor.BLUE + "/" + ChatColor.AQUA
                + totalPages;
        StringBuilder pageCommandSpacing = new StringBuilder();

        pageCommandSpacing.append(" ".repeat(Math.max(0, (PowerCamera.getInstance().getChatMaxLineLength()
                - ChatColor.stripColor(previousPageCommand).length() - ChatColor.stripColor(nextPageCommand).length()
                - ChatColor.stripColor(currentPageInfo).length()) / 2)));
        if (pageCommandSpacing.length() == 0) {
            pageCommandSpacing = new StringBuilder("  ");
        }
        sender.sendMessage(
                previousPageCommand + pageCommandSpacing + currentPageInfo + pageCommandSpacing + nextPageCommand);
        sender.sendMessage(PowerCamera.getInstance().getCommandFooter());
    }
}
