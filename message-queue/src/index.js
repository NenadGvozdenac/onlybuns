const express = require('express');

const app = express();
const PORT = process.env.PORT || 4000;

app.use(express.json());

class MessageQueue {
    constructor() {
        this.queues = new Map();
    }

    enqueue(queueName, message) {
        if (!this.queues.has(queueName)) {
            this.queues.set(queueName, []);
        }
        
        const queue = this.queues.get(queueName);
        queue.push({
            id: Date.now() + Math.random(),
            timestamp: new Date().toISOString(),
            data: message
        });

        console.log(`Message added to queue '${queueName}':`, message);
        return queue.length;
    }

    dequeue(queueName, limit = 10) {
        if (!this.queues.has(queueName)) {
            return [];
        }
        
        const queue = this.queues.get(queueName);
        const messages = [];
        
        for (let i = 0; i < limit && queue.length > 0; i++) {
            const message = queue.shift();
            messages.push(message.data);
        }
        
        if (messages.length > 0) {
            console.log(`Consumed ${messages.length} messages from queue '${queueName}'`);
        }
        
        return messages;
    }

    getQueueStatus(queueName) {
        const queue = this.queues.get(queueName) || [];
        
        return {
            queueName,
            messagesInQueue: queue.length,
            totalMessages: queue.length
        };
    }
}

const messageQueue = new MessageQueue();

app.post('/api/queue/:queueName/send', (req, res) => {
    const { queueName } = req.params;
    const message = req.body;
    
    const queueLength = messageQueue.enqueue(queueName, message);
    
    res.json({
        success: true,
        message: 'Message added to queue',
        queueName,
        queueLength,
        queueStatus: messageQueue.getQueueStatus(queueName)
    });
});

app.get('/api/queue/:queueName/consume', (req, res) => {
    const { queueName } = req.params;
    const limit = parseInt(req.query.limit) || 10;
    
    const messages = messageQueue.dequeue(queueName, limit);
    
    res.json({
        success: true,
        queueName,
        messages,
        messageCount: messages.length,
        queueStatus: messageQueue.getQueueStatus(queueName)
    });
});

app.listen(PORT, () => {
    console.log(`Message Queue Server running on port ${PORT}`);
});

module.exports = app;
