package com.qreal.wmp.uitesting.dia.scene;

import com.qreal.wmp.uitesting.dia.pallete.PalleteElement;
import com.qreal.wmp.uitesting.dia.pallete.PalleteImpl;
import com.qreal.wmp.uitesting.dia.scene.elements.Block;
import com.qreal.wmp.uitesting.dia.scene.elements.Link;
import com.qreal.wmp.uitesting.dia.scene.elements.SceneElement;
import com.qreal.wmp.uitesting.dia.scene.providers.BlockProvider;
import com.qreal.wmp.uitesting.dia.scene.providers.LinkProvider;
import com.qreal.wmp.uitesting.dia.scene.window.SceneWindow;
import com.qreal.wmp.uitesting.dia.scene.window.SceneWindowImpl;
import com.qreal.wmp.uitesting.exceptions.ElementNotOnTheSceneException;
import org.jetbrains.annotations.Contract;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.codeborne.selenide.Selenide.$;

/**
 * Describes Scene of Editor.
 * Can add rm and manipulate with objects on that area.
 */
public class SceneImpl implements Scene {

    private static final String SELECTOR = ".scene-wrapper";

    private static final Logger logger = LoggerFactory.getLogger(PalleteImpl.class);
    
    private final WebDriver webDriver;
    
    private final SceneWindow sceneWindow;
    
    private final BlockProvider blockProvider;
    
    private final LinkProvider linkProvider;
    
    /** For actions such as mouse move we need driver of current page. */
    private SceneImpl(WebDriver webDriver) {
        this.webDriver = webDriver;
        // For actions such as mouse move we need driver of current page.
        if (webDriver instanceof JavascriptExecutor) {
            ((JavascriptExecutor) webDriver).executeScript(
                    createDiv("SceneWindowLeft") + createDiv("SceneWindowTop") +
                            createDiv("SceneWindowHorSize") + createDiv("SceneWindowVerSize")
            );
        }
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }
        sceneWindow = SceneWindowImpl.getSceneWindow(this, webDriver, SELECTOR);
        blockProvider = BlockProvider.getBlockProvider(sceneWindow, SELECTOR);
        linkProvider = LinkProvider.getLinkProvider(SELECTOR, webDriver);
    }
    
    @Override
    public Block dragAndDrop(final PalleteElement element) {
        element.getInner().dragAndDropTo(SELECTOR);
        return blockProvider.getNewBlock();
    }
    
    @Override
    public Block dragAndDrop(final PalleteElement element, int cell_x, int cell_y) {
        Block newBlock = dragAndDrop(element);
        blockProvider.moveToCell(newBlock, cell_x, cell_y);
        return newBlock;
    }
    
    @Override
    public void moveToCell(Block block, final int cell_x, final int cell_y) {
        blockProvider.moveToCell(block, cell_x, cell_y);
    }
    
    @Override
    public boolean exist(SceneElement element) {
        if (element instanceof Block) {
            return blockProvider.exist((Block) element);
        }
        if (element instanceof Link) {
            return linkProvider.exist((Link) element);
        }
        return false;
    }
    
    @Override
    public void remove(SceneElement element) throws ElementNotOnTheSceneException {
        if (element instanceof Link) {
            removeSceneElement(((Link) element).getSource());
        } else {
            removeSceneElement(element);
        }
    }
    
    @Override
    public Link addLink(final Block source, final Block target) {
        return linkProvider.addLink(source, target);
    }
    
    @Override
    public List<Block> getBlocks() {
        return blockProvider.getBlocks();
    }
    
    @Override
    public void clean() {
        if (!linkProvider.isEmpty()) {
            try {
                remove(linkProvider.getLinks().get(0));
            } catch (ElementNotOnTheSceneException e) {
                logger.error(e.getMessage());
            }
            clean();
        } else {
            if (!blockProvider.isEmpty()) {
                try {
                    remove(blockProvider.getBlocks().get(0));
                } catch (ElementNotOnTheSceneException e) {
                    logger.error(e.getMessage());
                }
                clean();
            } else {
                logger.info("Clean scene");
            }
        }
    }
    
    @Contract("_ -> !null")
    public static Scene getScene(WebDriver webDriver) {
        return new SceneImpl(webDriver);
    }
    
    @Contract(pure = true)
    private static String createDiv(String divName) {
        return "$('body').append('<div id=\"" + divName + "\" style=\"position:absolute;visibility:hidden;\"></div>');";
    }
    
    @Contract("null -> fail")
    private void removeSceneElement(SceneElement sceneElement) throws ElementNotOnTheSceneException {
        sceneWindow.focus(sceneElement.getCoordinateOnScene());
        logger.info("Remove element {} form scene", sceneElement.getInnerSeleniumElement().toString());
        new Actions(webDriver).contextClick(sceneElement.getInnerSeleniumElement()).build().perform();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }
        $(By.id("scene-context-menu")).click();
        
        blockProvider.recalculateBlocks();
        linkProvider.recalculateLinks();
    }
}