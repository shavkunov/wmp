package com.qreal.robots.components.database.diagrams.service.server;

import com.qreal.robots.components.database.diagrams.service.client.DiagramServiceImpl;
import com.qreal.robots.components.database.diagrams.thrift.gen.DiagramDbService;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;

public class DiagramDbServer {

    private static final Logger logger = LoggerFactory.getLogger(DiagramDbServer.class);

    private static void runTServer(DiagramDbService.Processor processor) {
        int port = 9093;
        logger.info("Starting Diagram DB TServer on localhost on port {}", port);
        try {
            TServerTransport serverTransport = new TServerSocket(port);
            TServer server = new TSimpleServer(new TSimpleServer.Args(serverTransport).processor(processor));
            server.serve();
            logger.info("Diagram DB TServer started successfully");
        } catch (Exception e) {
            logger.error("DiagramDBServer encountered problem while starting TServer. TServer cannot be started.", e);
        }
    }

    public DiagramDbServer(AbstractApplicationContext context) {
        try {
            DiagramDbServiceHandler handler = new DiagramDbServiceHandler(context);
            DiagramDbService.Processor processor = new DiagramDbService.Processor(handler);

            Runnable runServer = () -> {
                runTServer(processor);
            };
            logger.trace("Creating new thread for Diagram DB TServer");
            new Thread(runServer).start();
            logger.trace("Thread created. Server started.");
        } catch (Exception x) {
            logger.error("DiagramDBServer encountered problem while creating TServer.", x);
        }
    }
}

