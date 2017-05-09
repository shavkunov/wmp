import {Property} from "./Property";

/**
 * Class that wraps text Joint object. Responsible for showing text on screen.
 */
export class PropertyEditElement {
    /**
     * Default size of text font.
     */
    public static fontSize = 20;

    /**
     * Text object which contains text information.
     */
    private textObject: joint.shapes.basic.Text;

    /**
     * Current x, y position of text object.
     */
    private currentX: number;
    private currentY: number;

    /**
     * Creates property at specified coordinates with specified name and value.
     * @param x x axis coordinate
     * @param y y axis coordinate
     * @param property property to set
     * @param graph graph for setting text on the plane.
     */
    constructor(x : number, y : number, property : Property, graph : joint.dia.Graph) {
        console.log("Property edit element constructor");

        this.currentX = x;
        this.currentY = y;
        this.setProperty(property, graph);

        this.textObject.on("cell:pointermove", (cellView, event, x, y): void => {
            this.setPosition(x, y);
        });
    }

    public getTextObject() : joint.shapes.basic.Text {
        return this.textObject;
    }

    getX(): number {
        return (this.textObject.get("position"))['x'];
    }

    getY(): number {
        return (this.textObject.get("position"))['y'];
    }

    /**
     * Sets the position of text.
     * @param x x axis coordinate
     * @param y y axis coordinate
     */
    public setPosition(x: number, y: number): void {
        console.log("Setting text position with x : " + x + " and y : " + y);
        this.textObject.position(x, y);
    }

    /**
     * Sets new property. Removes old and replace it with new one property on the same position.
     * @param property property to set.
     * @param graph graph, where text joint object is situated.
     */
    public setProperty(property : Property, graph : joint.dia.Graph): void {
        let width: number = 0.5 * (property.name.length + property.value.length) * PropertyEditElement.fontSize;
        let height: number = PropertyEditElement.fontSize;

        if (this.textObject) {
            this.textObject.remove();
        }

        this.textObject = new  joint.shapes.basic.Text({
            position: { x: this.currentX, y: this.currentY },
            size: { width: width, height: height },
            attrs: {
                text: {
                    text: property.name + " : " + property.value,
                },
            },
        });

        graph.addCell(this.textObject);
    }

    /**
     * Sets relative position. In other words: it moves text object by appending coordinates respectively.
     * @param deltaX x coordinate change
     * @param deltaY y coordinate change
     */
    public setRelativePosition(deltaX : number, deltaY : number): void {
        this.currentX = this.getX() + deltaX;
        this.currentY = this.getY() + deltaY;
        this.setPosition(this.currentX, this.currentY);
    }
}