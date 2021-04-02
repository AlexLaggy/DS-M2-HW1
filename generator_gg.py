import collections
import datetime
import random

status = {'debug': 7,
          'info': 6,
          'notice': 5,
          'warning': 4,
          'warn': 4,
          'err': 3,
          'error': 3,
          'crit': 2,
          'alert': 1,
          'emerg': 0,
          'panic': 0}

data = {}

for i in range(500_000):
    key = random.choice(list(status.keys()))
    date = datetime.datetime.now() - datetime.timedelta(hours=random.choice(range(0, 23)),
                                                        minutes=random.choice(range(0, 59)),
                                                        seconds=random.choice(range(0, 59)))
    data.update({date: f'{key},{status.get(key)}'})

with open('syslog.log', 'w') as f:
    for it, key in enumerate(data.keys()):
        if it % 113 == 0:
            f.write("It's a wrong,way,3\n")
        elif it % 1001 == 0:
            f.write("Bad joke!\n")
        if it == 499_998:
            break
        f.write(f'{key},{data.get(key)}\n')
