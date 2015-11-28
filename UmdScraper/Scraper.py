__author__ = 'JunjieW'

import urllib
import re
import HTMLParser
from datetime import datetime
from datetime import timedelta

# We need to write a input including the specific urls during
# a certina period, then iterate to get the html

## The 8-digits date argument in url means the week of a certain chart,
## according to billboard, the date of a chart is the last day of the chart.
## So if we search for 2009 06 06, it means the char is for 2009 05 31 - 2009 06 06

# str_url_prefix = 'http://www.umdmusic.com/default.asp?Lang=English&Chart=D&ChDate='
# str_init_date = '20090530'
# number_of_weeks = 20
# str_url = str_url_prefix + str_init_date
# print str_url + '\n'

# my_datetime = datetime.strptime(str_init_date, "%Y%m%d")
# print 'Confirm Intialization: ', my_datetime.strftime("%Y%m%d") + '\n'

# print '==== Start ====='
# my_datetime = datetime.strptime(str_init_date, "%Y%m%d")
# for i in range(1,number_of_weeks+1):
#     print my_datetime.strftime("%Y%m%d")
#     my_datetime += timedelta(days=7)

class BoardTableParser(HTMLParser.HTMLParser):
    def __init__(self, str_outfile, bool_only_get_title):
        HTMLParser.HTMLParser.__init__(self)
        self.in_td = False
        self.in_tr = False
        self.start_print = False
        self.row_number = 0
        self.col_number = 0

        self.str_outfile_path_indicated_by_date = str_outfile
        self.outfile = open(str_outfile, 'w')
        self.out_buffer = ''

        self.is_only_get_title = bool_only_get_title


    def handle_comment(self, data):
        if data.find("Display Chart Table") > 0:
            self.start_print = True
            #print "============= find comment =================="

    def handle_starttag(self, tag, attrs):
        if tag == 'td':
            self.in_td = True
            if self.start_print == True:
                self.col_number += 1

        if tag == 'tr':
            self.in_tr = True
            if self.start_print == True:
                self.row_number += 1 # print contorl
                self.col_number = 0 # print control, new line


    # If have data, this method will be invoke
    def handle_data(self, data):
        if self.start_print == True:
            if self.in_td and self.row_number >=3:
                # Regular expression replaces multiple continuing \t space \f to only one
                pattern = re.compile('\s+')
                str_data = re.sub(pattern,' ',data)
                str_data = str_data.strip()
                # use str.strip method to remove the front and end space in the string
                #print str_data.strip() +',',
                if self.col_number < 10 :
                    # TODO: Here is a bug, some of the singer name with non a-z character will be separated into several comma slot
                    #print str_data.strip(), '[', self.col_number, ']', ',',
                    print str_data.strip(),',',
                    self.putIntoBuffer(str_data)
                    if self.col_number == 5:
                        self.col_number += 1
                else:
                    print str_data.strip(),
                    if not self.is_only_get_title:
                        self.out_buffer += str_data.strip() + '\n'


    def handle_endtag(self, tag):
        if tag == 'td' :
            self.in_td = False
        if tag == 'tr' and self.row_number >=3: # read without column title
            print ''
            self.in_tr = False
        if tag ==  'html':
            self.outfile.write('# Week ' + self.str_outfile_path_indicated_by_date + '\n')
            self.outfile.write("This Week Pos, Last Week Pos, This Week Peak, This Week Total Week, Title, Artist, Entry Date, Entry Pos, Peak Pos, Total Weeks\n")
            self.outfile.write(self.out_buffer)

    def putIntoBuffer(self, str_data):
        if self.is_only_get_title:
            if self.col_number == 5:
                self.out_buffer += str_data.strip() + '\n'
        else:
            if self.col_number == 5 or self.col_number == 6:
                self.out_buffer += r'"' + str_data.strip() + r'",'
            else:
                self.out_buffer += str_data.strip() + ','


# =========================== For debug BoardTableParser ===============================
# url = 'http://www.umdmusic.com/default.asp?Lang=English&Chart=D&ChDate=' +  '20090530'
# print url
# html_socket = urllib.urlopen(url)
# li = html_socket.read()
# html_socket.close()
# bbt_parser = BoardTableParser("sss")
# bbt_parser.feed(li)
# ======================== End For debug BoardTableParser ==============================


def gethtml(str_url):
    html_socket = urllib.urlopen(str_url)
    f_html = html_socket.read()
    html_socket.close()
    return f_html


def getBillboardData(str_start, num_weeks):
    # Initial url, intitial data, and iterations setting
    str_url_prefix = 'http://www.umdmusic.com/default.asp?Lang=English&Chart=D&ChDate='
    str_init_date = str_start
    number_of_weeks = num_weeks
    my_datetime = datetime.strptime(str_init_date, "%Y%m%d")

    only_get_song_title = True

    # Start iteration
    for i in range(1,number_of_weeks+1):
        str_url = str_url_prefix + my_datetime.strftime("%Y%m%d")
        print my_datetime.strftime("%Y%m%d")
        print str_url
        bbt_parser = BoardTableParser(str(my_datetime.strftime("%Y%m%d")), only_get_song_title)
        bbt_parser.feed(gethtml(str_url))
        my_datetime += timedelta(days=7)


getBillboardData('20090530', 5)


# # TODO: wirte data into text file rather than in console