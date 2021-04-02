import logging
import logging.handlers
from sys import platform
import os
import random
import time

logger = logging.getLogger()
logger.setLevel(logging.DEBUG)
if platform == 'darwin':
    handler = logging.handlers.SysLogHandler(address='/var/run/syslog', facility='local1')
else:
    handler = logging.handlers.SysLogHandler(address='/dev/log')
logger.addHandler(handler)


def log_now():
    for i in range(100_000):
        k = random.choice(range(4))
        if not k:
            logger.debug('this is debug')
        elif k == 1:
            logger.critical('this is critical')
        elif k == 2:
            logger.error('this is error')
        elif k == 3:
            logger.warning('this is warning')
        else:
            logger.info('this is info')
        if not i % 50_000:
            os.system('cat /var/log/system.log > ~/PycharmProjects/DS-M20/generated.log')
            time.sleep(0.3)
        elif not i % 500:
            time.sleep(0.3)


if __name__ == '__main__':
    log_now()
