/*
 * Copyright (c) 2016. Papyrus Electronics, Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * you may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taptrack.tcmptappy.utils;

import android.content.Context;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.support.annotation.NonNull;

import com.taptrack.tappyble.R;
import com.taptrack.tcmptappy.tappy.constants.TagTypes;
import com.taptrack.tcmptappy.tcmp.TCMPMessage;
import com.taptrack.tcmptappy.tcmp.commandfamilies.basicnfc.commands.GetBasicNfcLibraryVersionCommand;
import com.taptrack.tcmptappy.tcmp.commandfamilies.basicnfc.commands.ScanNdefCommand;
import com.taptrack.tcmptappy.tcmp.commandfamilies.basicnfc.commands.ScanTagCommand;
import com.taptrack.tcmptappy.tcmp.commandfamilies.basicnfc.commands.StreamNdefCommand;
import com.taptrack.tcmptappy.tcmp.commandfamilies.basicnfc.commands.StreamTagsCommand;
import com.taptrack.tcmptappy.tcmp.commandfamilies.basicnfc.commands.WriteNdefTextRecordCommand;
import com.taptrack.tcmptappy.tcmp.commandfamilies.basicnfc.commands.WriteNdefUriRecordCommand;
import com.taptrack.tcmptappy.tcmp.commandfamilies.basicnfc.responses.BasicNfcErrorResponse;
import com.taptrack.tcmptappy.tcmp.commandfamilies.basicnfc.responses.BasicNfcLibraryVersionResponse;
import com.taptrack.tcmptappy.tcmp.commandfamilies.basicnfc.responses.NdefFoundResponse;
import com.taptrack.tcmptappy.tcmp.commandfamilies.basicnfc.responses.ScanTimeoutResponse;
import com.taptrack.tcmptappy.tcmp.commandfamilies.basicnfc.responses.TagFoundResponse;
import com.taptrack.tcmptappy.tcmp.commandfamilies.basicnfc.responses.TagWrittenResponse;
import com.taptrack.tcmptappy.tcmp.commandfamilies.systemfamily.commands.GetBatteryLevelCommand;
import com.taptrack.tcmptappy.tcmp.commandfamilies.systemfamily.commands.GetFirmwareVersionCommand;
import com.taptrack.tcmptappy.tcmp.commandfamilies.systemfamily.commands.GetHardwareVersionCommand;
import com.taptrack.tcmptappy.tcmp.commandfamilies.systemfamily.commands.PingCommand;
import com.taptrack.tcmptappy.tcmp.commandfamilies.systemfamily.responses.CrcMismatchErrorResponse;
import com.taptrack.tcmptappy.tcmp.commandfamilies.systemfamily.responses.FirmwareVersionResponse;
import com.taptrack.tcmptappy.tcmp.commandfamilies.systemfamily.responses.GetBatteryLevelResponse;
import com.taptrack.tcmptappy.tcmp.commandfamilies.systemfamily.responses.HardwareVersionResponse;
import com.taptrack.tcmptappy.tcmp.commandfamilies.systemfamily.responses.ImproperMessageFormatResponse;
import com.taptrack.tcmptappy.tcmp.commandfamilies.systemfamily.responses.LcsMismatchErrorResponse;
import com.taptrack.tcmptappy.tcmp.commandfamilies.systemfamily.responses.LengthMismatchErrorResponse;
import com.taptrack.tcmptappy.tcmp.commandfamilies.systemfamily.responses.PingResponse;

import java.nio.charset.Charset;
import java.util.Arrays;

public class TcmpMessageDescriptor {
    public static String getCommandDescription(@NonNull TCMPMessage command,
                                               @NonNull Context ctx) {
        if(command instanceof GetBasicNfcLibraryVersionCommand) {
            return ctx.getString(R.string.get_basic_nfc_lib_version);
        }
        else if (command instanceof ScanNdefCommand) {
            if(((ScanNdefCommand) command).getTimeout() != 0) {
                String form = ctx.getString(R.string.scan_ndef_seconds);
                return String.format(form,(0xff&((ScanNdefCommand) command).getTimeout()));
            }
            else {
                return ctx.getString(R.string.scan_ndef_indefinite);
            }
        }
        else if (command instanceof StreamNdefCommand) {
            if(((StreamNdefCommand) command).getDuration() != 0) {
                String form = ctx.getString(R.string.stream_ndef_seconds);
                return String.format(form,(0xff&((StreamNdefCommand) command).getDuration()));
            }
            else {
                return ctx.getString(R.string.stream_ndef_indefinite);
            }
        }
        else if (command instanceof ScanTagCommand) {
            if(((ScanTagCommand) command).getTimeout() != 0) {
                String form = ctx.getString(R.string.scan_tag_seconds);
                return String.format(form,(0xff&((ScanTagCommand) command).getTimeout()));
            }
            else {
                return ctx.getString(R.string.scan_ndef_indefinite);
            }

        }
        else if (command instanceof StreamTagsCommand) {
            if(((StreamTagsCommand) command).getDuration() != 0) {
                String form = ctx.getString(R.string.stream_tag_seconds);
                return String.format(form,(0xff&((StreamTagsCommand) command).getDuration()));
            }
            else {
                return ctx.getString(R.string.stream_tag_indefinitely);
            }
        }
        else if (command instanceof WriteNdefTextRecordCommand) {
            WriteNdefTextRecordCommand cmd = (WriteNdefTextRecordCommand) command;
            if(cmd.getDuration() != 0) {
                String form = ctx.getString(R.string.write_ndef_txt_seconds);
                return String.format(form, (0xff & (cmd.getDuration())), new String(cmd.getText()));
            }
            else {
                String form = ctx.getString(R.string.write_ndef_txt_indefinite);
                return String.format(form,new String(cmd.getText()));
            }
        }
        else if (command instanceof WriteNdefUriRecordCommand) {
            WriteNdefUriRecordCommand cmd = (WriteNdefUriRecordCommand) command;
            String uri = NdefUriCodeUtils.decodeNdefUri(cmd.getUriCode(),cmd.getUri());
            if(cmd.getDuration() != 0) {
                String form = ctx.getString(R.string.write_ndef_uri_seconds);
                return String.format(form, (0xff & (cmd.getDuration())),uri);
            }
            else {
                String form = ctx.getString(R.string.write_ndef_uri_indefinite);
                return String.format(form,uri);
            }
        }
        else if (command instanceof GetBatteryLevelCommand) {
            return ctx.getString(R.string.get_battery_level);
        }
        else if (command instanceof GetFirmwareVersionCommand) {
            return ctx.getString(R.string.get_firmware_version);
        }
        else if (command instanceof GetHardwareVersionCommand) {
            return ctx.getString(R.string.get_hardware_version);
        }
        else if (command instanceof PingCommand) {
            return ctx.getString(R.string.ping_command);
        }

        return ctx.getString(R.string.unknown_command);
    }


    public static String getResponseDescription(@NonNull TCMPMessage response,
                                                @NonNull Context ctx) {
        if(response instanceof CrcMismatchErrorResponse) {
            return ctx.getString(R.string.crc_error);
        }
        else if(response instanceof FirmwareVersionResponse) {
            FirmwareVersionResponse resp = (FirmwareVersionResponse) response;
            return String.format(
                    ctx.getString(R.string.firmware_version_response),
                    (0xff&resp.getMajorVersion()),
                    (0xff&resp.getMinorVersion()));
        }
        else if(response instanceof GetBatteryLevelResponse) {
            GetBatteryLevelResponse resp = (GetBatteryLevelResponse) response;
            return String.format(
                    ctx.getString(R.string.get_batt_response),
                    resp.getBatteryLevelPercent());

        }
        else if(response instanceof HardwareVersionResponse) {
            HardwareVersionResponse resp = (HardwareVersionResponse) response;
            return String.format(
                    ctx.getString(R.string.hardware_version_response),
                    (0xff&resp.getMajorVersion()),
                    (0xff&resp.getMinorVersion()));
        }
        else if(response instanceof ImproperMessageFormatResponse) {
            return ctx.getString(R.string.improper_message_format_response);

        }
        else if(response instanceof LcsMismatchErrorResponse) {
            return ctx.getString(R.string.lcs_mismatch_response);

        }
        else if(response instanceof LengthMismatchErrorResponse) {
            return ctx.getString(R.string.length_check_failed);

        }
        else if(response instanceof PingResponse) {
            return ctx.getString(R.string.ping_response);

        }
        else if(response instanceof BasicNfcErrorResponse) {
            BasicNfcErrorResponse resp = (BasicNfcErrorResponse) response;
            return String.format(
                    ctx.getString(R.string.basic_nfc_error_response),
                    resp.getErrorCode(),
                    resp.getInternalError(),
                    resp.getPn532Status(),
                    resp.getErrorMessage());
        }
        else if(response instanceof BasicNfcLibraryVersionResponse) {
            BasicNfcLibraryVersionResponse resp = (BasicNfcLibraryVersionResponse) response;
            return String.format(
                    ctx.getString(R.string.basic_nfc_library_version_response),
                    (0xff & resp.getMajorVersion()),
                    (0xff & resp.getMinorVersion()));
        }
        else if(response instanceof NdefFoundResponse) {
            NdefFoundResponse resp = (NdefFoundResponse) response;
            return parseNdefFoundResponse(ctx,resp);
        }
        else if(response instanceof ScanTimeoutResponse) {
            return ctx.getString(R.string.scan_timeout_response);
        }
        else if(response instanceof TagFoundResponse) {
            TagFoundResponse resp = (TagFoundResponse) response;
            return String.format(
                    ctx.getString(R.string.tag_found_response),
                    ByteUtils.bytesToHex(resp.getTagCode()),
                    parseTagType(ctx, resp.getTagType()));
        }
        else if(response instanceof TagWrittenResponse) {
            TagWrittenResponse resp = (TagWrittenResponse) response;
            return String.format(
                    ctx.getString(R.string.tag_written_response),
                    ByteUtils.bytesToHex(resp.getTagCode()));
        }


        return ctx.getString(R.string.unknown_response);
    }

    private static String parseNdefFoundResponse(Context ctx, NdefFoundResponse resp) {
        NdefMessage msg = resp.getNdefMessage();
        NdefRecord[] records = msg.getRecords();
        if(records.length == 0) {
            return ctx.getString(R.string.ndef_no_record);
        }
        else if (records.length == 1) {
            return String.format(ctx.getString(R.string.ndef_found_response_single_record),
                    ByteUtils.bytesToHex(resp.getTagCode()),
                    parseTagType(ctx, resp.getTagType()),
                    parseNdefRecord(ctx, records[0]));
        }
        else {
            return String.format(ctx.getString(R.string.ndef_found_response_multi_record),
                    ByteUtils.bytesToHex(resp.getTagCode()),
                    parseTagType(ctx, resp.getTagType()),
                    parseNdefRecord(ctx, records[0]));
        }
    }

    private static String parseNdefRecord(Context ctx, NdefRecord record) {
        if(record.getTnf() == NdefRecord.TNF_WELL_KNOWN) {
            if(Arrays.equals(record.getType(),NdefRecord.RTD_URI)) {
                return parseWellKnownUriRecord(ctx,record);
            }
            else if (Arrays.equals(record.getType(),NdefRecord.RTD_TEXT)) {
                return parseWellKnownTextRecord(ctx, record);
            }
            else {
                return parseGenericNdefRecord(ctx,record);
            }
        }
        else {
            return parseGenericNdefRecord(ctx,record);
        }
    }

    private static String parseWellKnownTextRecord(Context ctx, NdefRecord record) {

        byte[] payload = record.getPayload();

        int status = payload[0] & 0xff;
        int languageCodeLength = (status & 0x1F);
        //not needed currently
        //String languageCode = new String(payload, 1, languageCodeLength);

        Charset textEncoding = ((status & 0x80) != 0) ? Charset.forName("UTF-16") : Charset.forName("UTF-8");
        String field = new String(payload, 1 + languageCodeLength, payload.length - languageCodeLength - 1, textEncoding);
        return ctx.getString(R.string.ndef_record_text_display_format,field);
    }

    private static String parseWellKnownUriRecord(Context ctx, NdefRecord record) {
        byte[] payload = record.getPayload();
        if(payload.length > 1) {
            byte uriCode = payload[0];
            byte[] uri = new byte[payload.length - 1];
            System.arraycopy(payload,1,uri,0,payload.length - 1);
            return ctx.getString(R.string.ndef_record_uri_display_format, NdefUriCodeUtils.decodeNdefUri(uriCode, uri));
        }
        else {
            return parseGenericNdefRecord(ctx,record);
        }
    }

    private static String parseGenericNdefRecord(Context ctx, NdefRecord record) {
        return String.format(ctx.getString(R.string.ndef_record_generic_display_format),
                parseTnf(ctx,record),
                parseType(ctx,record),
                ByteUtils.bytesToHex(record.getPayload()));
    }

    private static String parseTnf(Context ctx, NdefRecord record) {
        short tnf = record.getTnf();
        switch(tnf) {
            case NdefRecord.TNF_ABSOLUTE_URI:
                return ctx.getString(R.string.tnf_abs_uri);
            case NdefRecord.TNF_EMPTY:
                return ctx.getString(R.string.tnf_empty);
            case NdefRecord.TNF_EXTERNAL_TYPE:
                return ctx.getString(R.string.tnf_external);
            case NdefRecord.TNF_MIME_MEDIA:
                return ctx.getString(R.string.tnf_mime_media);
            case NdefRecord.TNF_UNCHANGED:
                return ctx.getString(R.string.tnf_unchanged);
            case NdefRecord.TNF_WELL_KNOWN:
                return ctx.getString(R.string.tnf_well_known);
            default:
                return ctx.getString(R.string.tnf_unknown);
        }
    }

    private static String parseType(Context ctx, NdefRecord record) {
        byte[] type = record.getType();
        if(Arrays.equals(type, NdefRecord.RTD_URI))
            return ctx.getString(R.string.rtd_uri);
        else if(Arrays.equals(type,NdefRecord.RTD_ALTERNATIVE_CARRIER))
            return ctx.getString(R.string.rtd_alt_carrier);
        else if(Arrays.equals(type,NdefRecord.RTD_HANDOVER_CARRIER))
            return ctx.getString(R.string.rtd_handover_carrier);
        else if(Arrays.equals(type,NdefRecord.RTD_HANDOVER_REQUEST))
            return ctx.getString(R.string.rtd_handover_request);
        else if(Arrays.equals(type,NdefRecord.RTD_HANDOVER_SELECT))
            return ctx.getString(R.string.rtd_handover_select);
        else if(Arrays.equals(type,NdefRecord.RTD_SMART_POSTER))
            return ctx.getString(R.string.rtd_smart_poster);
        else if(Arrays.equals(type,NdefRecord.RTD_TEXT))
            return ctx.getString(R.string.rtd_text);
        else if (record.getTnf() == NdefRecord.TNF_MIME_MEDIA)
            return new String(type);
        else
            return ByteUtils.bytesToHex(type);
    }

    private static String parseTagType(Context ctx, byte flag) {
        switch(flag) {
            case TagTypes.MIFARE_ULTRALIGHT: {
                return ctx.getString(R.string.ultralight_title);
            }
            case TagTypes.NTAG203: {
                return ctx.getString(R.string.ntag203_title);
            }
            case TagTypes.MIFARE_ULTRALIGHT_C: {
                return ctx.getString(R.string.ultralight_c_title);
            }
            case TagTypes.MIFARE_STD_1K: {
                return ctx.getString(R.string.std_1k_title);
            }
            case TagTypes.MIFARE_STD_4K: {
                return ctx.getString(R.string.std_4k_title);
            }
            case TagTypes.MIFARE_DESFIRE_EV1_2K: {
                return ctx.getString(R.string.desfire_ev1_2k_title);
            }
            case TagTypes.TYPE_2_TAG: {
                return ctx.getString(R.string.unk_type2_title);
            }
            case TagTypes.MIFARE_PLUS_2K_CL2: {
                return ctx.getString(R.string.plus_2k_title);
            }
            case TagTypes.MIFARE_PLUS_4K_CL2: {
                return ctx.getString(R.string.plus_4k_title);
            }
            case TagTypes.MIFARE_MINI: {
                return ctx.getString(R.string.mini_title);
            }
            case TagTypes.OTHER_TYPE4: {
                return ctx.getString(R.string.other_type4_title);
            }
            case TagTypes.MIFARE_DESFIRE_EV1_4K: {
                return ctx.getString(R.string.desfire_ev1_4k_title);
            }
            case TagTypes.MIFARE_DESFIRE_EV1_8K: {
                return ctx.getString(R.string.desfire_ev1_8k);
            }
            case TagTypes.MIFARE_DESFIRE: {
                return ctx.getString(R.string.desfire_title);
            }
            case TagTypes.TOPAZ_512: {
                return ctx.getString(R.string.topaz_512_title);
            }
            case TagTypes.NTAG_210: {
                return ctx.getString(R.string.ntag_210_title);
            }
            case TagTypes.NTAG_212: {
                return ctx.getString(R.string.ntag_212_title);
            }
            case TagTypes.NTAG_213: {
                return ctx.getString(R.string.ntag_213_title);
            }
            case TagTypes.NTAG_215: {
                return ctx.getString(R.string.ntag_215_title);
            }
            case TagTypes.NTAG_216: {
                return ctx.getString(R.string.ntag_216_title);
            }
            case TagTypes.NO_TAG: {
                return ctx.getString(R.string.no_tag_title);
            }
            case TagTypes.TAG_UNKNOWN:
            default: {
                return ctx.getString(R.string.unk_type_title);
            }
        }
    }
}
