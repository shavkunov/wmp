import {Property} from "./Property";

export class PropertyEditElement {
    public static fontSize = 20;

    private textObject: joint.shapes.basic.Text;
    private lastX: number;
    private lastY: number;

    /**
     * Creates property at specified coordinates with specified name and value.
     * @param x x axis coordinate
     * @param y y axis coordinate
     * @param property property to set
     * @param graph graph for setting text on the plane.
     */
    constructor(x : number, y : number, property : Property, graph : joint.dia.Graph) {
        console.log("Property edit element constructor");

        this.lastX = x;
        this.lastY = y;
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

    public setPosition(x: number, y: number): void {
        console.log("Setting text position with x : " + x + " and y : " + y);
        this.textObject.position(x, y);
    }

    public setProperty(property : Property, graph : joint.dia.Graph): void {
        var width: number = 0.5 * (property.name.length + property.value.length) * PropertyEditElement.fontSize;
        var height: number = PropertyEditElement.fontSize;

        if (this.textObject) {
            this.lastX = this.getX();
            this.lastY = this.getY();
            this.textObject.remove();
        }

        this.textObject = new  joint.shapes.basic.Text({
            position: { x: this.lastX, y: this.lastY },
            size: { width: width, height: height },
            attrs: {
                text: {
                    text: property.name + " : " + property.value,
                },
            },
        });

        graph.addCell(this.textObject);
    }

    public setRelativePosition(deltaX : number, deltaY : number): void {
        this.lastX = this.getX() + deltaX;
        this.lastY = this.getY() + deltaY;
        this.setPosition(this.lastX, this.lastY);
    }
}