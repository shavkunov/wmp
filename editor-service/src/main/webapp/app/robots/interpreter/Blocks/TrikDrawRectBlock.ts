import {Parser} from "../Parser";
import {Interpreter} from "../Interpreter";
import {AbstractBlock} from "./AbstractBlock";
import {Link} from "core/editorCore/model/Link";
import {DiagramNode} from "core/editorCore/model/DiagramNode";
import {RobotModel} from "../../twoDModel/interfaces/engine/model/RobotModel";
export class TrikDrawRectBlock extends AbstractBlock {

    private interpreter: Interpreter;
    private robotModels: RobotModel[];
    private EXPECTED_NUMBER_OF_OUTBOUND_LINKS = 1;

    constructor(node: DiagramNode, outboundLinks: Link[], interpreter: Interpreter, robotModels: RobotModel[]) {
        super(node, outboundLinks);
        this.interpreter = interpreter;
        this.robotModels = robotModels;
    }

    public run(): void {
        var output = this.node.getName(); + "\n";
        this.checkExpectedNumberOfOutboundLinks(this.EXPECTED_NUMBER_OF_OUTBOUND_LINKS);

        var properties = this.node.getChangeableProperties();
        var parser = new Parser();
        var x = parser.parseExpression(properties["XCoordinateRect"].value, this.interpreter);
        var y = parser.parseExpression(properties["YCoordinateRect"].value, this.interpreter);
        var width = parser.parseExpression(properties["WidthRect"].value, this.interpreter);
        var height = parser.parseExpression(properties["HeightRect"].value, this.interpreter);

        for (var modelId = 0; modelId < this.robotModels.length; modelId++) {
            var model = this.robotModels[modelId];
            model.getDisplayWidget().drawRectangle(x, y, width, height,
                this.interpreter.getEnvironmentVariable("painterColor"),
                this.interpreter.getEnvironmentVariable("painterWidth"));
        }

        console.log(output);
    }

    public getNextNodeId(): string {
        return this.outboundLinks[0].getJointObject().get('target').id;
    }

}