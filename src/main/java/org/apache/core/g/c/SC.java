package org.apache.core.g.c;

import org.apache.core.g.w.W;
import org.apache.core.m.ms.c.CG;
import org.apache.core.t.IFont;
import org.apache.core.u.MU;
import org.apache.core.u.RU;
import org.apache.core.Client;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static org.apache.core.Client.MC;

public class SC extends C {

    private double width;
    private double value;
    private final double min, max, step;
    private final DisplayType displayType;
    private final Consumer<Double> action;
    private final Supplier<Boolean> availability;

    public SC(W parent, double x, double y, double width, double value, double min, double max, double step, DisplayType displayType, Consumer<Double> action, Supplier<Boolean> availability, String name) {
        super(parent, x, y, 10, name);
        this.width = width;
        this.action = action;
        this.value = value;
        this.min = min;
        this.max = max;
        this.step = step;
        this.displayType = displayType;
        this.availability = availability;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);
        renderBackGround(matrices);
        renderSlider(matrices);
        renderValue(matrices);
    }

    private void renderBackGround(MatrixStack matrices) {
        double parentX = parent.getX();
        double parentY = parent.getY();
        double parentWidth = parent.getWidth();
        double parentLength = parent.getLength();
        double parentX2 = parent.getX() + parentWidth;
        double parentY2 = parent.getY() + parentLength;
        double x = getX() + parentX;
        double y = Math.max(getY() + parentY, parentY);
        double x2 = x + width;
        double y2 = Math.min(getY() + parentY + 10, parentY2);
        if (getY() + 10 <= 0)
            return;
        if (parentY2 - (getY() + parentY) <= 0)
            return;
        RenderSystem.setShader(GameRenderer::getPositionShader);
        RenderSystem.setShaderColor(0.6f, 0.6f, 0.6f, 1.0f);
        RU.drawQuad(x, y + 4.5f, x + width, y + 5f, matrices);
    }

    private void renderSlider(MatrixStack matrices) {
        double r = CG.class.cast(Client.INSTANCE.moduleManager().getModule(CG.class)).getHudColorRed();
        double g = CG.class.cast(Client.INSTANCE.moduleManager().getModule(CG.class)).getHudColorGreen();
        double b = CG.class.cast(Client.INSTANCE.moduleManager().getModule(CG.class)).getHudColorBlue();
        double offset = (value - min) / (max - min) * width;
        double parentX = parent.getX();
        double parentY = parent.getY();
        double parentWidth = parent.getWidth();
        double parentLength = parent.getLength();
        double parentX2 = parent.getX() + parentWidth;
        double parentY2 = parent.getY() + parentLength;
        double x = getX() + parentX + offset - 2;
        double y = Math.max(getY() + parentY, parentY);
        double x2 = x + 4;
        double y2 = Math.min(getY() + parentY + 10, parentY2);
        if (getY() + 10 <= 0)
            return;
        if (parentY2 - (getY() + parentY) <= 0)
            return;
        RenderSystem.setShader(GameRenderer::getPositionShader);
        RenderSystem.setShaderColor(0.4f, 0.4f, 0.4f, 1.0f);
        if (availability.get())
            RenderSystem.setShaderColor((float) r, (float) g, (float) b, 1.0f);
        RU.drawQuad(x, y, x2, y2, matrices);
    }

    private void renderValue(MatrixStack matrices) {
        double parentX = parent.getX();
        double parentY = parent.getY();
        double parentWidth = parent.getWidth();
        double parentLength = parent.getLength();
        double parentX2 = parent.getX() + parentWidth;
        double parentY2 = parent.getY() + parentLength;
        double x = getX() + parentX + width / 2;
        double y = Math.max(getY() + parentY, parentY);
        if (getY() + 10 <= 0)
            return;
        if (parentY2 - (getY() + parentY) <= 0)
            return;
        String display = null;
        switch (displayType) {
            case DECIMAL -> display = String.format("%.3f", value);
            case INTEGER -> display = String.valueOf((int) value);
        }


        if (CG.class.cast(Client.INSTANCE.moduleManager().getModule(CG.class)).customFont.get()) {
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
            IFont.COMFORTAA.drawString(matrices, display, (float) (x + width - 20), (float) y, new Color(255, 255, 255).getRGB(), false);
        } else {
            MC.textRenderer.draw(matrices, display, (float) (x + width - 20), (float) y, 0xffffff);
        }
    }

    private void slide(double mouseX, double mouseY) {
        if (!availability.get())
            return;
        double parentX = parent.getX();
        double parentY = parent.getY();
        double parentWidth = parent.getWidth();
        double parentLength = parent.getLength();
        double parentX2 = parent.getX() + parentWidth;
        double parentY2 = parent.getY() + parentLength;
        double x = getX() + parentX;
        double y = Math.max(getY() + parentY, parentY);
        double x2 = x + width;
        double y2 = Math.min(getY() + parentY + 10, parentY2);
        if (getY() + 10 <= 0)
            return;
        if (parentY2 - (getY() + parentY) <= 0)
            return;
        if (RU.isHoveringOver(mouseX, mouseY, x, y, x2, y2)) {
            value = (mouseX - x) / width * (max - min) + min;
            value = MU.roundToStep(value, step);
            if (value < min)
                value = min;
            if (value > max)
                value = max;
            action.accept(value);
        }
    }

    @Override
    public void onMouseMoved(double mouseX, double mouseY) {
        if (GLFW.glfwGetMouseButton(MC.getWindow().getHandle(), GLFW.GLFW_MOUSE_BUTTON_1) != GLFW.GLFW_PRESS && GLFW.glfwGetMouseButton(MC.getWindow().getHandle(), GLFW.GLFW_MOUSE_BUTTON_2) != GLFW.GLFW_PRESS)
            return;
        slide(mouseX, mouseY);
    }

    @Override
    public void onMouseClicked(double mouseX, double mouseY, int button) {
        slide(mouseX, mouseY);
    }

    @Override
    public boolean onMouseScrolled(double mouseX, double mouseY, double amount) {
        if (!availability.get())
            return false;
        double parentX = parent.getX();
        double parentY = parent.getY();
        double parentWidth = parent.getWidth();
        double parentLength = parent.getLength();
        double parentX2 = parent.getX() + parentWidth;
        double parentY2 = parent.getY() + parentLength;
        double x = getX() + parentX;
        double y = Math.max(getY() + parentY, parentY);
        double x2 = x + width;
        double y2 = Math.min(getY() + parentY + 10, parentY2);
        if (getY() + 10 <= 0)
            return false;
        if (parentY2 - (getY() + parentY) <= 0)
            return false;
        if (RU.isHoveringOver(mouseX, mouseY, x, y, x2, y2)) {
            value += -amount * step;
            value = MU.roundToStep(value, step);
            if (value < min)
                value = min;
            if (value > max)
                value = max;
            action.accept(value);
            return true;
        }
        return super.onMouseScrolled(mouseX, mouseY, amount);
    }

    public enum DisplayType {
        DECIMAL,
        INTEGER,
        DEGREE
    }
}
