/*
 * Copyright 2019 Anton Tananaev (anton@traccar.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.traccar.protocol;

import org.traccar.StringProtocolEncoder;
import org.traccar.model.Command;
import org.traccar.helper.Checksum;

public class LaipacProtocolEncoder extends StringProtocolEncoder {

    @Override
    protected String formatCommand(Command command, String format, String... keys) {
        String sentence = super.formatCommand(command, "$" + format, keys);
        sentence += Checksum.nmea(sentence) + "\r\n";
        return sentence;
    }

    @Override
    protected Object encodeCommand(Command command) {

        initDevicePassword(command, LaipacProtocolDecoder.DEFAULT_DEVICE_PASSWORD);

        switch (command.getType()) {
            case Command.TYPE_CUSTOM:
                return formatCommand(command, "{%s}",
                    Command.KEY_DATA);
            case Command.TYPE_POSITION_SINGLE:
                return formatCommand(command, "AVREQ,{%s},1",
                    Command.KEY_DEVICE_PASSWORD);
            case Command.TYPE_REBOOT_DEVICE:
                return formatCommand(command, "AVRESET,{%s},{%s}",
                    Command.KEY_UNIQUE_ID, Command.KEY_DEVICE_PASSWORD);
            default:
                return null;
        }

    }

}